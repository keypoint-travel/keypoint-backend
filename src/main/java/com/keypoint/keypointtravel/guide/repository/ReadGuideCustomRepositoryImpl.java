package com.keypoint.keypointtravel.guide.repository;

import com.keypoint.keypointtravel.global.entity.QUploadFile;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.guide.dto.response.ReadGuideInAdminResponse;
import com.keypoint.keypointtravel.guide.entity.QGuide;
import com.keypoint.keypointtravel.guide.entity.QGuideTranslation;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class ReadGuideCustomRepositoryImpl implements ReadGuideCustomRepository {

    private final JPAQueryFactory queryFactory;

    private final QGuide guide = QGuide.guide;
    private final QGuideTranslation translation = QGuideTranslation.guideTranslation;
    private final QUploadFile uploadFile = QUploadFile.uploadFile;

    @Override
    public Page<ReadGuideInAdminResponse> findGuidesInAdmin(Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        builder
            .and(translation.guide.id.eq(guide.id))
            .and(translation.languageCode.eq(LanguageCode.EN))
            .and(translation.isDeleted.eq(false));

        List<ReadGuideInAdminResponse> data = queryFactory
            .select(
                Projections.fields(
                    ReadGuideInAdminResponse.class,
                    guide.id.as("guideId"),
                    translation.title,
                    translation.subTitle,
                    uploadFile.path.as("thumbnailImageUrl"),
                    translation.content,
                    guide.order,
                    translation.modifyAt
                )
            )
            .from(guide)
            .leftJoin(translation).on(builder)
            .innerJoin(uploadFile).on(uploadFile.id.eq(guide.thumbnailImageId))
            .where(guide.isDeleted.eq(false))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long count = queryFactory
            .select(
                guide.count()
            )
            .where(guide.isDeleted.eq(false))
            .from(guide)
            .fetchOne();

        return new PageImpl<>(data, pageable, count);
    }
}
