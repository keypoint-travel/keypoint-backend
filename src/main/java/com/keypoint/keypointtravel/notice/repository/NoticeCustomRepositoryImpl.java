package com.keypoint.keypointtravel.notice.repository;

import com.keypoint.keypointtravel.global.dto.useCase.PageAndMemberIdUseCase;
import com.keypoint.keypointtravel.global.entity.QUploadFile;
import com.keypoint.keypointtravel.global.enumType.error.NoticeErrorCode;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.notice.dto.response.NoticeDetailResponse;
import com.keypoint.keypointtravel.notice.dto.response.NoticeResponse;
import com.keypoint.keypointtravel.notice.dto.response.adminNoticeDetail.AdminNoticeContentResponse;
import com.keypoint.keypointtravel.notice.dto.response.adminNoticeDetail.AdminNoticeDetailResponse;
import com.keypoint.keypointtravel.notice.dto.useCase.DeleteNoticeContentUseCase;
import com.keypoint.keypointtravel.notice.dto.useCase.DeleteNoticeContentsUseCase;
import com.keypoint.keypointtravel.notice.dto.useCase.DeleteNoticeUseCase;
import com.keypoint.keypointtravel.notice.dto.useCase.UpdateNoticeContentUseCase;
import com.keypoint.keypointtravel.notice.entity.QNotice;
import com.keypoint.keypointtravel.notice.entity.QNoticeContent;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class NoticeCustomRepositoryImpl implements NoticeCustomRepository {

    private final JPAQueryFactory queryFactory;
    private final QNotice notice = QNotice.notice;
    private final QNoticeContent noticeContent = QNoticeContent.noticeContent;
    private final AuditorAware<String> auditorProvider;
    private final QUploadFile uploadFile = QUploadFile.uploadFile;

    @Override
    public boolean isExistNoticeContentByLanguageCode(Long noticeId, LanguageCode languageCode) {
        Long content = queryFactory
            .select(noticeContent.id)
            .from(noticeContent)
            .where(noticeContent.notice.id.eq(noticeId)
                .and(noticeContent.isDeleted.isFalse())
                .and(noticeContent.languageCode.eq(languageCode)))
            .fetchOne();

        return content != null;
    }

    @Override
    public Page<NoticeResponse> findNotices(PageAndMemberIdUseCase useCase,
        LanguageCode languageCode) {
        Pageable pageable = useCase.getPageable();
        String sortBy = useCase.getSortBy();
        String direction = useCase.getDirection();

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(notice.isDeleted.eq(false))
            .and(noticeContent.isDeleted.eq(false));

        QUploadFile activeFile = new QUploadFile("activeFile");

        // 동적 정렬 기준 생성
        List<OrderSpecifier<?>> orderSpecifiers = getOrderSpecifiers(sortBy, direction);

        // Query 실행
        List<NoticeResponse> result = queryFactory
            .select(
                Projections.constructor(
                    NoticeResponse.class,
                    notice.id,
                    noticeContent.id,
                    noticeContent.title,
                    noticeContent.content,
                    activeFile.path.as("thumbnailImageUrl"),
                    noticeContent.languageCode,
                    noticeContent.createAt,
                    noticeContent.modifyAt
                )
            )
            .from(notice)
            .innerJoin(notice.noticeContents, noticeContent)
            .on(noticeContent.languageCode.eq(languageCode).and(noticeContent.isDeleted.isFalse()))
            .leftJoin(activeFile).on(activeFile.id.eq(notice.thumbnailImageId))
            .where(builder)
            .orderBy(orderSpecifiers.toArray(new OrderSpecifier<?>[0]))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long count = queryFactory
            .select(
                notice.count()
            )
            .from(notice)
            .innerJoin(notice.noticeContents, noticeContent)
            .on(noticeContent.languageCode.eq(languageCode).and(noticeContent.isDeleted.isFalse()))
            .where(builder)
            .fetchOne();

        return new PageImpl<>(result, pageable, count);
    }

    private List<OrderSpecifier<?>> getOrderSpecifiers(String sortBy, String direction) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        if (sortBy != null) {
            Order order = direction.equalsIgnoreCase("asc") ? Order.ASC : Order.DESC;

            switch (sortBy) {
                case "noticeId":
                    orderSpecifiers.add(new OrderSpecifier<>(order, notice.id));
                    orderSpecifiers.add(new OrderSpecifier<>(order, noticeContent.id));
                    break;
                case "title":
                    orderSpecifiers.add(new OrderSpecifier<>(order, noticeContent.title));
                    break;
                case "content":
                    orderSpecifiers.add(new OrderSpecifier<>(order, noticeContent.content));
                    break;
                case "createAt":
                    orderSpecifiers.add(new OrderSpecifier<>(order, noticeContent.createAt));
                    break;
            }
        } else { //기본 정렬 기준
            orderSpecifiers.add(new OrderSpecifier<>(Order.ASC, noticeContent.modifyAt));
        }
        return orderSpecifiers;
    }

    @Override
    public void updateNoticeContent(UpdateNoticeContentUseCase useCase) {
        String currentAuditor = auditorProvider.getCurrentAuditor().orElse(null);

        queryFactory
            .update(noticeContent)
            .set(noticeContent.title, useCase.getTitle())
            .set(noticeContent.content, useCase.getContent())
            .set(noticeContent.languageCode, useCase.getLanguageCode())

            .set(noticeContent.modifyAt, LocalDateTime.now())
            .set(noticeContent.modifyId, currentAuditor)
            .where(noticeContent.id.eq(useCase.getNoticeContentId()))
            .execute();
    }

    @Override
    public NoticeDetailResponse findNoticeByNoticeContentId(Long noticeContentId) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(noticeContent.id.eq(noticeContentId))
            .and(noticeContent.isDeleted.eq(false));

        QUploadFile activeFile = new QUploadFile("activeFile");

        // NoticeContent를 조회하여 상세 이미지들을 가져옴
        return queryFactory
            .select(
                Projections.fields(
                    NoticeDetailResponse.class,
                    noticeContent.notice.id.as("noticeId"),
                    noticeContent.id.as("noticeContentId"),
                    noticeContent.title,
                    noticeContent.content,
                    activeFile.path.as("thumbnailImageUrl"),
                    noticeContent.languageCode,
                    noticeContent.createAt,
                    noticeContent.modifyAt
                )
            )
            .from(noticeContent)
            .leftJoin(activeFile).on(activeFile.id.eq(noticeContent.notice.thumbnailImageId))
            .where(builder)
            .fetchOne();
    }

    @Transactional
    @Override
    public void deleteNoticeContents(DeleteNoticeContentsUseCase useCase) {
        String currentAuditor = auditorProvider.getCurrentAuditor().orElse(null);

        // 공지 내용 삭제
        queryFactory.update(noticeContent)
            .set(noticeContent.isDeleted, true)
            .set(noticeContent.modifyAt, LocalDateTime.now())
            .set(noticeContent.modifyId, currentAuditor)
            .where(noticeContent.id.in(useCase.getNoticeContentIds()))
            .execute();
    }

    @Override
    public long deleteNoticeContent(DeleteNoticeContentUseCase useCase) {
        String currentAuditor = auditorProvider.getCurrentAuditor().orElse(null);

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(noticeContent.id.eq(useCase.getNoticeContentId()))
            .and(noticeContent.notice.id.eq(useCase.getNoticeId()));

        // 공지 내용 삭제
        return queryFactory.update(noticeContent)
            .set(noticeContent.isDeleted, true)
            .set(noticeContent.modifyAt, LocalDateTime.now())
            .set(noticeContent.modifyId, currentAuditor)
            .where(builder)
            .execute();
    }

    @Override
    public AdminNoticeDetailResponse findNoticeById(Long noticeId) {
        BooleanBuilder guideBuilder = new BooleanBuilder();
        guideBuilder.and(notice.id.eq(noticeId))
            .and(notice.isDeleted.eq(false));

        BooleanBuilder translationBuilder = new BooleanBuilder();
        translationBuilder
            .and(noticeContent.notice.id.eq(notice.id))
            .and(noticeContent.isDeleted.eq(false));

        List<AdminNoticeDetailResponse> data = queryFactory
            .selectFrom(notice)
            .leftJoin(noticeContent).on(translationBuilder)
            .innerJoin(uploadFile).on(uploadFile.id.eq(notice.thumbnailImageId))
            .where(guideBuilder)
            .transform(GroupBy.groupBy(notice.id)
                .list(
                    Projections.fields(
                        AdminNoticeDetailResponse.class,
                        notice.id.as("noticeId"),
                        uploadFile.path.as("thumbnailImageUrl"),
                        GroupBy.list(
                            Projections.fields(
                                AdminNoticeContentResponse.class,
                                noticeContent.id.as("noticeContentId"),
                                noticeContent.languageCode,
                                noticeContent.title,
                                noticeContent.content
                            )
                        ).as("translations")
                    )
                )
            );

        if (data == null || data.isEmpty()) {
            throw new GeneralException(NoticeErrorCode.NOT_EXISTED_NOTICE);
        }

        return data.get(0);
    }

    @Transactional
    @Override
    public void deleteNotices(DeleteNoticeUseCase useCase) {
        String currentAuditor = auditorProvider.getCurrentAuditor().orElse(null);

        // 1. 공지 삭제
        queryFactory.update(notice)
            .set(notice.isDeleted, true)
            .set(notice.modifyAt, LocalDateTime.now())
            .set(notice.modifyId, currentAuditor)
            .where(notice.id.in(useCase.getNoticeIds()))
            .execute();

        // 2. 관련된 NoticeContent 삭제
        queryFactory.update(noticeContent)
            .set(noticeContent.isDeleted, true)
            .set(noticeContent.modifyAt, LocalDateTime.now())
            .set(noticeContent.modifyId, currentAuditor)
            .where(noticeContent.notice.id.in(useCase.getNoticeIds()))
            .execute();
    }



}
