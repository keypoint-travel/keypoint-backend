package com.keypoint.keypointtravel.badge.respository;

import com.keypoint.keypointtravel.badge.dto.useCase.DeleteBadgeUseCase;
import com.keypoint.keypointtravel.badge.dto.useCase.UpdateBadgeUseCase;
import com.keypoint.keypointtravel.badge.entity.QBadge;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;

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
}
