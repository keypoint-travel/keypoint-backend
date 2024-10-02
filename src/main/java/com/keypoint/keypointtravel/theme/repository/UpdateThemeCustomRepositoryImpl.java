package com.keypoint.keypointtravel.theme.repository;

import com.keypoint.keypointtravel.theme.dto.useCase.UpdateThemeUseCase;
import com.keypoint.keypointtravel.theme.entity.QPaidTheme;
import com.keypoint.keypointtravel.theme.entity.QTheme;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;

@RequiredArgsConstructor
public class UpdateThemeCustomRepositoryImpl implements UpdateThemeCustomRepository{

    private final JPAQueryFactory queryFactory;
    private final AuditorAware<String> auditorProvider;

    private final QTheme theme = QTheme.theme;
    private final QPaidTheme paidTheme = QPaidTheme.paidTheme;

    @Override
    public long updateTheme(UpdateThemeUseCase useCase) {
        String currentAuditor = auditorProvider.getCurrentAuditor().orElse(null);

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(theme.id.eq(useCase.getThemeId()))
            .and(theme.isDeleted.eq(false));

        // 테마 업데이트
        return queryFactory.update(theme)
            .set(theme.name, useCase.getName())
            .set(theme.color, useCase.getColor())
            .set(theme.modifyAt, LocalDateTime.now())
            .set(theme.modifyId, currentAuditor)
            .where(builder)
            .execute();
    }

    @Override
    public long updatePaidTheme(UpdateThemeUseCase useCase) {
        String currentAuditor = auditorProvider.getCurrentAuditor().orElse(null);

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(paidTheme.id.eq(useCase.getThemeId()))
            .and(paidTheme.isDeleted.eq(false));

        // 테마 업데이트
        return queryFactory.update(paidTheme)
            .set(paidTheme.name, useCase.getName())
            .set(paidTheme.color, useCase.getColor())
            .set(paidTheme.modifyAt, LocalDateTime.now())
            .set(paidTheme.modifyId, currentAuditor)
            .where(builder)
            .execute();
    }

}
