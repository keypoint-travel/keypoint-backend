package com.keypoint.keypointtravel.guide.repository;

import com.keypoint.keypointtravel.global.dto.useCase.PageAndMemberIdUseCase;
import com.keypoint.keypointtravel.global.dto.useCase.PageUseCase;
import com.keypoint.keypointtravel.global.entity.QUploadFile;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.guide.dto.response.ReadGuideInAdminResponse;
import com.keypoint.keypointtravel.guide.dto.response.ReadGuideResponse;
import com.keypoint.keypointtravel.guide.dto.response.readGuideDetail.ReadGuideDetailResponse;
import com.keypoint.keypointtravel.guide.dto.response.readGuideDetail.ReadNextGuideResponse;
import com.keypoint.keypointtravel.guide.dto.response.readGuideDetailInAdmin.ReadGuideDetailInAdminResponse;
import com.keypoint.keypointtravel.guide.dto.response.readGuideDetailInAdmin.ReadGuideTranslationInAdminResponse;
import com.keypoint.keypointtravel.guide.entity.QGuide;
import com.keypoint.keypointtravel.guide.entity.QGuideTranslation;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
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
    public Page<ReadGuideInAdminResponse> findGuidesInAdmin(PageUseCase useCase) {
        Pageable pageable = useCase.getPageable();
        String sortBy = useCase.getSortBy();
        String direction = useCase.getDirection();

        BooleanBuilder builder = new BooleanBuilder();
        builder
            .and(translation.guide.id.eq(guide.id))
            .and(translation.languageCode.eq(LanguageCode.EN))
            .and(translation.isDeleted.eq(false));

        // 기본 정렬 기준 추가
        OrderSpecifier<?> orderSpecifier = new OrderSpecifier<>(Order.ASC, guide.order);

        // 동적 정렬 기준 처리
        if (sortBy != null) {
            Order order = direction.equals("asc") ? Order.ASC : Order.DESC;

            switch (sortBy) {
                case "guideId":
                    orderSpecifier = new OrderSpecifier<>(order, guide.id);
                    break;
                case "title":
                    orderSpecifier = new OrderSpecifier<>(order, translation.title);
                    break;
                case "subTitle":
                    orderSpecifier = new OrderSpecifier<>(order, translation.subTitle);
                    break;
                case "order":
                    orderSpecifier = new OrderSpecifier<>(order, guide.order);
                    break;
                case "content":
                    orderSpecifier = new OrderSpecifier<>(order, translation.content);
                    break;
                case "modifyAt":
                    orderSpecifier = new OrderSpecifier<>(order, translation.modifyAt);
                    break;
            }
        }

        List<ReadGuideInAdminResponse> data = queryFactory
            .select(
                Projections.constructor(
                    ReadGuideInAdminResponse.class,
                    guide.id,
                    translation.title,
                    translation.subTitle,
                    uploadFile.path,
                    translation.content,
                    guide.order,
                    translation.modifyAt
                )
            )
            .from(guide)
            .leftJoin(translation).on(builder)
            .innerJoin(uploadFile).on(uploadFile.id.eq(guide.thumbnailImageId))
            .where(guide.isDeleted.eq(false))
            .orderBy(orderSpecifier)
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

    @Override
    public ReadGuideDetailInAdminResponse findGuideDetailInAdmin(Long guideId) {
        BooleanBuilder guideBuilder = new BooleanBuilder();
        guideBuilder.and(guide.id.eq(guideId))
            .and(guide.isDeleted.eq(false));

        BooleanBuilder translationBuilder = new BooleanBuilder();
        translationBuilder
            .and(translation.guide.id.eq(guide.id))
            .and(translation.isDeleted.eq(false));

        List<ReadGuideDetailInAdminResponse> data = queryFactory
            .selectFrom(guide)
            .leftJoin(translation).on(translationBuilder)
            .innerJoin(uploadFile).on(uploadFile.id.eq(guide.thumbnailImageId))
            .where(guideBuilder)
            .transform(GroupBy.groupBy(guide.id)
                .list(
                    Projections.fields(
                        ReadGuideDetailInAdminResponse.class,
                        guide.id.as("guideId"),
                        uploadFile.path.as("thumbnailImageUrl"),
                        guide.order,
                        GroupBy.list(
                            Projections.fields(
                                ReadGuideTranslationInAdminResponse.class,
                                translation.id.as("guideTranslationId"),
                                translation.languageCode,
                                translation.title,
                                translation.subTitle,
                                translation.content
                            )
                        ).as("translations")
                    )
                )
            );

        return data == null ? null : data.get(0);
    }

    @Override
    public Page<ReadGuideResponse> findGuides(
        LanguageCode languageCode,
        PageAndMemberIdUseCase useCase
    ) {
        Pageable pageable = useCase.getPageable();
        String sortBy = useCase.getSortBy();
        String direction = useCase.getDirection();

        BooleanBuilder builder = new BooleanBuilder();
        builder
            .and(translation.guide.id.eq(guide.id))
            .and(translation.languageCode.eq(languageCode))
            .and(translation.isDeleted.eq(false));

        // 기본 정렬 기준 추가
        OrderSpecifier<?> orderSpecifier = new OrderSpecifier<>(Order.ASC, guide.order);

        // 동적 정렬 기준 처리
        if (sortBy != null) {
            Order order = direction.equals("asc") ? Order.ASC : Order.DESC;

            switch (sortBy) {
                case "guideTranslationIds":
                    orderSpecifier = new OrderSpecifier<>(order, translation.id);
                    break;
                case "title":
                    orderSpecifier = new OrderSpecifier<>(order, translation.title);
                    break;
                case "subTitle":
                    orderSpecifier = new OrderSpecifier<>(order, translation.subTitle);
                    break;
                case "order":
                    orderSpecifier = new OrderSpecifier<>(order, guide.order);
                    break;
            }
        }

        List<ReadGuideResponse> data = queryFactory
            .select(
                Projections.fields(
                    ReadGuideResponse.class,
                    translation.id.as("guideTranslationIds"),
                    translation.title,
                    translation.subTitle,
                    uploadFile.path.as("thumbnailImageUrl"),
                    guide.order
                )
            )
            .from(guide)
            .innerJoin(translation).on(builder)
            .innerJoin(uploadFile).on(uploadFile.id.eq(guide.thumbnailImageId))
            .where(guide.isDeleted.eq(false))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(orderSpecifier)
            .fetch();

        long count = queryFactory
            .select(
                guide.count()
            )
            .from(guide)
            .innerJoin(translation).on(builder)
            .innerJoin(uploadFile).on(uploadFile.id.eq(guide.thumbnailImageId))
            .where(guide.isDeleted.eq(false))
            .fetchOne();

        return new PageImpl<>(data, pageable, count);
    }

    @Override
    public ReadGuideDetailResponse findGuideDetail(
        Long guideTranslationIds
    ) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(translation.id.eq(guideTranslationIds))
            .and(translation.isDeleted.eq(false));

        return queryFactory
            .select(
                Projections.fields(
                    ReadGuideDetailResponse.class,
                    translation.id.as("guideTranslationIds"),
                    translation.title,
                    translation.subTitle,
                    uploadFile.path.as("thumbnailImageUrl"),
                    translation.content,
                    translation.guide.order
                )
            )
            .from(translation)
            .innerJoin(uploadFile).on(uploadFile.id.eq(guide.thumbnailImageId))
            .where(builder)
            .fetchFirst();
    }

    @Override
    public ReadNextGuideResponse findNextGuide(int order, LanguageCode languageCode) {
        BooleanBuilder translationBuilder = new BooleanBuilder();
        translationBuilder
            .and(translation.guide.id.eq(guide.id))
            .and(translation.isDeleted.eq(false))
            .and(translation.languageCode.eq(languageCode));

        return queryFactory
            .select(
                Projections.fields(
                    ReadNextGuideResponse.class,
                    translation.id.as("guideTranslationIds"),
                    translation.title,
                    translation.subTitle,
                    guide.order,
                    uploadFile.path.as("thumbnailImageUrl")
                )
            )
            .from(guide)
            .innerJoin(translation).on(translationBuilder)
            .innerJoin(uploadFile).on(uploadFile.id.eq(guide.thumbnailImageId))
            .where(guide.order.gt(order))
            .orderBy(guide.order.asc())
            .fetchFirst();
    }
}
