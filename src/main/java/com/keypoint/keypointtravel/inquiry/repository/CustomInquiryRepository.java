package com.keypoint.keypointtravel.inquiry.repository;

import com.keypoint.keypointtravel.global.entity.QUploadFile;
import com.keypoint.keypointtravel.inquiry.dto.response.AdminInquiriesResponse;
import com.keypoint.keypointtravel.inquiry.dto.response.ImageUrlResponse;
import com.keypoint.keypointtravel.inquiry.dto.response.UserInquiriesResponse;
import com.keypoint.keypointtravel.inquiry.dto.dto.UserInquiryDto;
import com.keypoint.keypointtravel.inquiry.dto.useCase.InquiriesUseCase;
import com.keypoint.keypointtravel.inquiry.entity.InquiryDetail;
import com.keypoint.keypointtravel.inquiry.entity.QInquiry;
import com.keypoint.keypointtravel.inquiry.entity.QInquiryDetail;
import com.keypoint.keypointtravel.inquiry.entity.QInquiryDetailImage;
import com.keypoint.keypointtravel.member.entity.QMember;
import com.keypoint.keypointtravel.member.entity.QMemberDetail;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomInquiryRepository {

    private final JPAQueryFactory queryFactory;

    private final QInquiry inquiry = QInquiry.inquiry;

    private final QInquiryDetail inquiryDetail = QInquiryDetail.inquiryDetail;

    private final QInquiryDetailImage inquiryDetailImage = QInquiryDetailImage.inquiryDetailImage;

    private final QMember member = QMember.member;

    private final QMemberDetail memberDetail = QMemberDetail.memberDetail;

    private final QUploadFile uploadFile = QUploadFile.uploadFile;

    public List<UserInquiryDto> findInquiryByUser(Long inquiryId, Long memberId) {
        return queryFactory.selectFrom(inquiryDetail)
            .leftJoin(inquiryDetailImage).on(inquiryDetailImage.inquiryDetail.id.eq(inquiryDetail.id))
            .leftJoin(uploadFile).on(inquiryDetailImage.inquiryImageId.eq(uploadFile.id))
            .where(inquiry.id.eq(inquiryId)
                .and(inquiry.member.id.eq(memberId)))
            .transform(GroupBy.groupBy(inquiryDetail.id)
                .list(Projections.constructor(
                    UserInquiryDto.class,
                    inquiryDetail.createAt,
                    inquiryDetail.inquiryContent,
                    inquiryDetail.isAdminReply,
                    GroupBy.list(Projections.constructor(
                        ImageUrlResponse.class,
                        uploadFile.path)))
                )
            );
    }

    public List<UserInquiriesResponse> findInquiriesByUser(Long memberId) {
        return queryFactory.select(
                Projections.constructor(
                    UserInquiriesResponse.class,
                    inquiry.id,
                    inquiry.isReplied,
                    inquiry.inquiryTitle,
                    inquiry.createAt))
            .from(inquiry)
            .where(inquiry.member.id.eq(memberId)
                .and(inquiry.isDeleted.eq(false)))
            .orderBy(inquiry.createAt.desc())
            .fetch();
    }

    // 1:1 문의 목록 조회(관리자)
    public Page<AdminInquiriesResponse> findInquiriesByAdmin(InquiriesUseCase useCase) {
        // order
        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(useCase.getSortBy(), useCase.getDirection());
        // query
        List<AdminInquiriesResponse> data = queryFactory.select(
                Projections.constructor(
                    AdminInquiriesResponse.class,
                    inquiry.id,
                    inquiry.isReplied,
                    member.id,
                    uploadFile.path,
                    member.name,
                    inquiry.inquiryTitle,
                    inquiry.createAt,
                    inquiry.modifyAt,
                    inquiry.isDeleted))
            .from(inquiry)
            .innerJoin(inquiry.member, member)
            .innerJoin(member.memberDetail, memberDetail)
            .leftJoin(uploadFile).on(memberDetail.profileImageId.eq(uploadFile.id))
            .innerJoin(inquiryDetail).on(inquiry.id.eq(inquiryDetail.inquiry.id))
            .where(useCase.getContent() != null ? inquiryDetail.inquiryContent.contains(useCase.getContent()) : null)
            .orderBy(orderSpecifier)
            .offset(useCase.getPageable().getOffset())
            .limit(useCase.getPageable().getPageSize())
            .fetch();
        Long count = queryFactory
            .select(inquiry.count())
            .from(inquiry)
            .fetchOne();

        return new PageImpl<>(data, useCase.getPageable(), count);
    }

    private OrderSpecifier<?> getOrderSpecifier(String sortBy, String direction) {
        // direction
        Order order = direction.equals("asc") ? Order.ASC : Order.DESC;
        // default order
        OrderSpecifier<?> orderSpecifier = new OrderSpecifier<>(order, inquiry.modifyAt);
        // 기준에 따라 sort 수정 : "memberName", "content", "isReplied", "modifyAt", "isDeleted"
        if (sortBy != null) {
            switch (sortBy) {
                case "memberName":
                    orderSpecifier = new OrderSpecifier<>(order, member.name);
                    break;
                case "content":
                    orderSpecifier = new OrderSpecifier<>(order, inquiry.inquiryTitle);
                    break;
                case "isReplied":
                    orderSpecifier = new OrderSpecifier<>(order, inquiry.isReplied);
                    break;
                case "isDeleted":
                    orderSpecifier = new OrderSpecifier<>(order, inquiry.isDeleted);
            }
        }
        return orderSpecifier;
    }

    // inqueryDetail을 image를 fetch join하여 조회
    public InquiryDetail findInquiryDetailWithImages(Long inquiryDetailId) {
        return queryFactory.selectFrom(inquiryDetail)
            .leftJoin(inquiryDetailImage).on(inquiryDetailImage.inquiryDetail.id.eq(inquiryDetail.id))
            .fetchJoin()
            .where(inquiryDetail.id.eq(inquiryDetailId))
            .fetchOne();
    }
}
