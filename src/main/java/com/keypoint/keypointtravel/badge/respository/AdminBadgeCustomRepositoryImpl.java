package com.keypoint.keypointtravel.badge.respository;

import com.keypoint.keypointtravel.badge.dto.request.BadgeIdRequest;
import com.keypoint.keypointtravel.badge.dto.response.BadgeInAdminResponse;
import com.keypoint.keypointtravel.badge.dto.useCase.DeleteBadgeUseCase;
import com.keypoint.keypointtravel.badge.dto.useCase.UpdateBadgeUseCase;
import com.keypoint.keypointtravel.badge.entity.QBadge;
import com.keypoint.keypointtravel.global.dto.useCase.PageUseCase;
import com.keypoint.keypointtravel.global.entity.QUploadFile;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class AdminBadgeCustomRepositoryImpl implements AdminBadgeCustomRepository {

    private final JPAQueryFactory queryFactory;
    private final AuditorAware<String> auditorProvider;

    private final QBadge badge = QBadge.badge;

    @Override
    public void updateBadge(UpdateBadgeUseCase useCase) {
        String currentAuditor = auditorProvider.getCurrentAuditor().orElse(null);

        queryFactory
            .update(badge)
            .set(badge.name, useCase.getName())
            .set(badge.order, useCase.getOrder())

            .set(badge.modifyAt, LocalDateTime.now())
            .set(badge.modifyId, currentAuditor)
            .where(badge.id.eq(useCase.getBadgeId()))
            .execute();
    }

    @Override
    public void deleteGuides(DeleteBadgeUseCase useCase) {
        String currentAuditor = auditorProvider.getCurrentAuditor().orElse(null);

        // 배지 삭제
        queryFactory.update(badge)
            .set(badge.isDeleted, true)
            .set(badge.modifyAt, LocalDateTime.now())
            .set(badge.modifyId, currentAuditor)

            .where(badge.id.in(useCase.getBadgeIds()))
            .execute();
    }

    @Override
    public BadgeInAdminResponse findBadgeById(BadgeIdRequest useCase) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(badge.id.eq(useCase.getBadgeId()))
            .and(badge.isDeleted.eq(false));

        QUploadFile activeFile = new QUploadFile("activeFile");
        QUploadFile inactiveFile = new QUploadFile("inactiveFile");

        return queryFactory
            .select(
                Projections.fields(
                    BadgeInAdminResponse.class,
                    badge.id.as("badgeId"),
                    badge.name,
                    badge.order,
                    activeFile.path.as("badgeOnImageUrl"),
                    inactiveFile.path.as("badgeOffImageUrl")
                )
            )
            .from(badge)
            .leftJoin(activeFile).on(activeFile.id.eq(badge.activeImageId))
            .leftJoin(inactiveFile).on(inactiveFile.id.eq(badge.inactiveImageId))
            .where(builder)
            .fetchFirst();
    }

    @Override
    public Page<BadgeInAdminResponse> findBadges(PageUseCase useCase) {
        Pageable pageable = useCase.getPageable();
        String sortBy = useCase.getSortBy();
        String direction = useCase.getDirection();
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(badge.isDeleted.eq(false));

        QUploadFile activeFile = new QUploadFile("activeFile");
        QUploadFile inactiveFile = new QUploadFile("inactiveFile");

        // 기본 정렬 기준 추가
        OrderSpecifier<?> orderSpecifier = new OrderSpecifier<>(Order.ASC, badge.order);

        // 동적 정렬 기준 처리
        if (sortBy != null) {
            Order order = direction.equals("asc") ? Order.ASC : Order.DESC;

            switch (sortBy) {
                case "name":
                    orderSpecifier = new OrderSpecifier<>(order, badge.name);
                    break;
                case "order":
                    orderSpecifier = new OrderSpecifier<>(order, badge.order);
                    break;
            }
        }

        List<BadgeInAdminResponse> result = queryFactory
            .select(
                Projections.fields(
                    BadgeInAdminResponse.class,
                    badge.id.as("badgeId"),
                    badge.name,
                    badge.order,
                    activeFile.path.as("badgeOnImageUrl"),
                    inactiveFile.path.as("badgeOffImageUrl")
                )
            )
            .from(badge)
            .leftJoin(activeFile).on(activeFile.id.eq(badge.activeImageId))
            .leftJoin(inactiveFile).on(inactiveFile.id.eq(badge.inactiveImageId))
            .where(builder)
            .orderBy(orderSpecifier)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        int count = queryFactory
            .select(badge)
            .from(badge)
            .where(builder)
            .fetch().size();

        return new PageImpl<>(result, pageable, count);
    }
}
