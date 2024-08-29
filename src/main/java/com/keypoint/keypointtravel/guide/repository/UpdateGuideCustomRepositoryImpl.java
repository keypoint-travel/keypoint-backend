package com.keypoint.keypointtravel.guide.repository;

import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.guide.dto.useCase.DeleteGuideGuideUseCase;
import com.keypoint.keypointtravel.guide.dto.useCase.DeleteGuideTranslationUseCase;
import com.keypoint.keypointtravel.guide.dto.useCase.updateGuide.UpdateGuideTranslationUseCase;
import com.keypoint.keypointtravel.guide.dto.useCase.updateGuide.UpdateGuideUseCase;
import com.keypoint.keypointtravel.guide.entity.QGuide;
import com.keypoint.keypointtravel.guide.entity.QGuideTranslation;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;

@RequiredArgsConstructor
public class UpdateGuideCustomRepositoryImpl implements UpdateGuideCustomRepository {

    private final JPAQueryFactory queryFactory;
    private final AuditorAware<String> auditorProvider;

    private final QGuide guide = QGuide.guide;
    private final QGuideTranslation guideTranslation = QGuideTranslation.guideTranslation;

    @Override
    public long updateGuide(UpdateGuideUseCase useCase) {
        String currentAuditor = auditorProvider.getCurrentAuditor().orElse(null);

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(guide.id.eq(useCase.getGuideId()))
            .and(guide.isDeleted.eq(false));

        // 이용 가이드 업데이트
        return queryFactory.update(guide)
            .set(guide.order, useCase.getOrder())

            .set(guide.modifyAt, LocalDateTime.now())
            .set(guide.modifyId, currentAuditor)
            .where(builder)
            .execute();
    }

    public long updateGuideTranslation(UpdateGuideTranslationUseCase useCase) {
        String currentAuditor = auditorProvider.getCurrentAuditor().orElse(null);

        // 이용 가이드 번역물 업데이트
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(guideTranslation.id.eq(useCase.getGuideTranslationId()))
            .and(guideTranslation.guide.id.eq(useCase.getGuideId()))
            .and(guideTranslation.isDeleted.eq(false));

        return queryFactory.update(guideTranslation)
            .set(guideTranslation.title, useCase.getTitle())
            .set(guideTranslation.subTitle, useCase.getSubTitle())
            .set(guideTranslation.content, useCase.getContent())
            .set(guideTranslation.languageCode, useCase.getLanguageCode())

            .set(guideTranslation.modifyAt, LocalDateTime.now())
            .set(guideTranslation.modifyId, currentAuditor)
            .where(builder)
            .execute();
    }

    @Override
    public void deleteGuides(DeleteGuideGuideUseCase useCase) {
        String currentAuditor = auditorProvider.getCurrentAuditor().orElse(null);

        // 이용 가이드 삭제
        queryFactory.update(guide)
            .set(guide.isDeleted, true)
            .set(guide.modifyAt, LocalDateTime.now())
            .set(guide.modifyId, currentAuditor)

            .where(guide.id.in(useCase.getGuideIds()))
            .execute();

        // 이용 가이드 번역물 삭제
        queryFactory.update(guideTranslation)
            .set(guideTranslation.isDeleted, true)
            .set(guideTranslation.modifyAt, LocalDateTime.now())
            .set(guideTranslation.modifyId, currentAuditor)

            .where(guideTranslation.guide.id.in(useCase.getGuideIds()))
            .execute();
    }

    @Override
    public long deleteGuideTranslations(DeleteGuideTranslationUseCase useCase) {
        String currentAuditor = auditorProvider.getCurrentAuditor().orElse(null);

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(guideTranslation.id.eq(useCase.getGuideTranslationId()))
            .and(guideTranslation.guide.id.eq(useCase.getGuideId()))
            .and(guideTranslation.languageCode.ne(LanguageCode.EN));

        return queryFactory.update(guideTranslation)
            .set(guideTranslation.isDeleted, true)
            .set(guideTranslation.modifyAt, LocalDateTime.now())
            .set(guideTranslation.modifyId, currentAuditor)

            .where(builder)
            .execute();
    }
}
