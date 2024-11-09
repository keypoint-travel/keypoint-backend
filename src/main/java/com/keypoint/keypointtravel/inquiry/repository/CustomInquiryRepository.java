package com.keypoint.keypointtravel.inquiry.repository;

import com.keypoint.keypointtravel.global.entity.QUploadFile;
import com.keypoint.keypointtravel.inquiry.dto.response.AdminInquiriesResponse;
import com.keypoint.keypointtravel.inquiry.dto.useCase.InquiriesUseCase;
import com.keypoint.keypointtravel.inquiry.entity.QInquiry;
import com.keypoint.keypointtravel.inquiry.entity.QInquiryDetail;
import com.keypoint.keypointtravel.member.entity.QMember;
import com.keypoint.keypointtravel.member.entity.QMemberDetail;
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

    private final QMember member = QMember.member;

    private final QMemberDetail memberDetail = QMemberDetail.memberDetail;

    private final QUploadFile uploadFile = QUploadFile.uploadFile;

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
}
