package com.keypoint.keypointtravel.theme.repository;

import com.keypoint.keypointtravel.theme.dto.useCase.DeleteThemeUseCase;
import com.keypoint.keypointtravel.theme.entity.QPaidTheme;
import com.keypoint.keypointtravel.theme.entity.QTheme;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class DeleteThemeCustomRepositoryImpl implements DeleteThemeCustomRepository{

    private final JPAQueryFactory queryFactory;

    private final AuditorAware<String> auditorProvider;

    private final QTheme theme = QTheme.theme;
    private final QPaidTheme paidTheme = QPaidTheme.paidTheme;


    @Transactional
    @Override
    public void deleteThemes(DeleteThemeUseCase useCase) {
        String currentAuditor = auditorProvider.getCurrentAuditor().orElse(null);

        queryFactory.update(theme)
            .set(theme.isDeleted, true)
            .set(theme.modifyAt, LocalDateTime.now())
            .set(theme.modifyId, currentAuditor)
            .where(theme.id.in(useCase.getThemeIds()))
            .execute();;
    }

    @Transactional
    @Override
    public void deletePaidThemes(DeleteThemeUseCase useCase) {
        String currentAuditor = auditorProvider.getCurrentAuditor().orElse(null);

        queryFactory.update(paidTheme)
            .set(paidTheme.isDeleted, true)
            .set(paidTheme.modifyAt, LocalDateTime.now())
            .set(paidTheme.modifyId, currentAuditor)
            .where(paidTheme.id.in(useCase.getThemeIds()))
            .execute();;
    }

}
