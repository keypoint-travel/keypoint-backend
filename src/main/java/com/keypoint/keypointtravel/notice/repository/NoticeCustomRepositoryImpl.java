package com.keypoint.keypointtravel.notice.repository;

import com.keypoint.keypointtravel.global.dto.useCase.PageUseCase;
import com.keypoint.keypointtravel.global.entity.QUploadFile;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.notice.dto.response.NoticesResponse;
import com.keypoint.keypointtravel.notice.entity.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class NoticeCustomRepositoryImpl implements NoticeCustomRepository {

    private final JPAQueryFactory queryFactory;

    private final QNotice notice = QNotice.notice;
    private final QNoticeContent noticeContent = QNoticeContent.noticeContent;

    @Override
    public boolean isExistNoticeContentByLanguageCode(Long noticeId, LanguageCode languageCode) {
        NoticeContent content = queryFactory.selectFrom(noticeContent)
            .where(noticeContent.notice.id.eq(noticeId)
                .and(noticeContent.isDeleted.isFalse())
                .and(noticeContent.languageCode.eq(languageCode)))
            .fetchOne();

        return content != null;
    }

    @Override
    public Page<NoticesResponse> findNotices(PageUseCase useCase) {
        Pageable pageable = useCase.getPageable();
        String sortBy = useCase.getSortBy();
        String direction = useCase.getDirection();
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(notice.isDeleted.eq(false));

        QUploadFile activeFile = new QUploadFile("activeFile");

        // 동적 정렬 기준 생성
        List<OrderSpecifier<?>> orderSpecifiers = getOrderSpecifiers(sortBy, direction);

        // Query 실행
        List<NoticesResponse> result = queryFactory
            .select(
                Projections.constructor(
                    NoticesResponse.class,
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
            .leftJoin(activeFile).on(activeFile.id.eq(noticeContent.thumbnailImageId))
            .where(builder)
            .orderBy(orderSpecifiers.toArray(new OrderSpecifier<?>[0]))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long count = queryFactory
            .select(notice)
            .from(notice)
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
}
