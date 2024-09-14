package com.keypoint.keypointtravel.notice.repository;

import com.keypoint.keypointtravel.global.dto.useCase.PageUseCase;
import com.keypoint.keypointtravel.global.entity.QUploadFile;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.notice.dto.response.NoticeDetailResponse;
import com.keypoint.keypointtravel.notice.dto.response.NoticeResponse;
import com.keypoint.keypointtravel.notice.dto.useCase.DeleteNoticeContentUseCase;
import com.keypoint.keypointtravel.notice.dto.useCase.DeleteNoticeContentsUseCase;
import com.keypoint.keypointtravel.notice.dto.useCase.DeleteNoticeUseCase;
import com.keypoint.keypointtravel.notice.dto.useCase.UpdateNoticeUseCase;
import com.keypoint.keypointtravel.notice.entity.NoticeContent;
import com.keypoint.keypointtravel.notice.entity.QNotice;
import com.keypoint.keypointtravel.notice.entity.QNoticeContent;
import com.querydsl.core.BooleanBuilder;
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
    public Page<NoticeResponse> findNotices(PageUseCase useCase) {
        Pageable pageable = useCase.getPageable();
        String sortBy = useCase.getSortBy();
        String direction = useCase.getDirection();
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(notice.isDeleted.eq(false));
        builder.and(noticeContent.isDeleted.eq(false));

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
            .leftJoin(notice.noticeContents, noticeContent)
            .leftJoin(activeFile).on(activeFile.id.eq(notice.thumbnailImageId))
            .where(builder)
            .orderBy(orderSpecifiers.toArray(new OrderSpecifier<?>[0]))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long count = queryFactory
            .select(noticeContent)
            .from(noticeContent)
            .where(builder)
            .fetchCount();

        return new PageImpl<>(result, pageable, count);
    }

    private List<OrderSpecifier<?>> getOrderSpecifiers(String sortBy, String direction) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        if (sortBy != null) {
            Order order = direction.equalsIgnoreCase("asc") ? Order.ASC : Order.DESC;

            switch (sortBy) {
                case "id":
                    orderSpecifiers.add(new OrderSpecifier<>(order, notice.id));
                    orderSpecifiers.add(new OrderSpecifier<>(order, noticeContent.id));
                    break;
            }
        } else { //기본 정렬 기준
            orderSpecifiers.add(new OrderSpecifier<>(Order.ASC, noticeContent.modifyAt));
        }
        return orderSpecifiers;
    }

    @Override
    public void updateNotice(UpdateNoticeUseCase useCase) {
        String currentAuditor = auditorProvider.getCurrentAuditor().orElse(null);
        queryFactory
            .update(noticeContent)
            .set(noticeContent.title, useCase.getTitle())
            .set(noticeContent.content, useCase.getContent())
            .set(noticeContent.modifyAt, LocalDateTime.now())
            .set(noticeContent.modifyId, currentAuditor)
            .where(noticeContent.id.eq(useCase.getNoticeContentId()))
            .execute();
    }

    @Override
    public NoticeDetailResponse findNoticeById(Long noticeContentId) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(noticeContent.id.eq(noticeContentId))
            .and(noticeContent.isDeleted.eq(false));

        QUploadFile activeFile = new QUploadFile("activeFile");

        // NoticeContent를 조회하여 상세 이미지들을 가져옴
        NoticeContent content = queryFactory
            .selectFrom(noticeContent)
            .leftJoin(noticeContent.notice, notice)
            .where(builder)
            .fetchOne();

        // 썸네일 이미지 URL 가져오기
        String thumbnailImageUrl = queryFactory
            .select(activeFile.path)
            .from(activeFile)
            //.where(activeFile.id.eq(content.getThumbnailImageId())) todo change
            .fetchOne();

        return new NoticeDetailResponse(
            content.getNotice().getId(),
            content.getId(),
            content.getTitle(),
            content.getContent(),
            thumbnailImageUrl,
            content.getLanguageCode(),
            content.getCreateAt(),
            content.getModifyAt()
        );
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
