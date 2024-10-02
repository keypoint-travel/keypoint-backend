package com.keypoint.keypointtravel.theme.repository;

import com.keypoint.keypointtravel.global.dto.useCase.PageUseCase;
import com.keypoint.keypointtravel.theme.dto.response.ThemeResponse;
import com.keypoint.keypointtravel.theme.entity.QPaidTheme;
import com.keypoint.keypointtravel.theme.entity.QTheme;
import com.keypoint.keypointtravel.theme.entity.QThemeColor;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.ExpressionUtils;
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
public class ThemeCustomRepositoryImpl implements ThemeCustomRepository {

    private final JPAQueryFactory queryFactory;
    private final QTheme theme = QTheme.theme;
    private final QPaidTheme paidTheme = QPaidTheme.paidTheme;

    private final QThemeColor themeColor = QThemeColor.themeColor;


    @Override
    public Page<ThemeResponse> findThemes(PageUseCase useCase) {
        Pageable pageable = useCase.getPageable();
        String sortBy = useCase.getSortBy();
        String direction = useCase.getDirection();

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(theme.isDeleted.eq(false));

        // 정렬 기준 추가
        List<OrderSpecifier<?>> orderSpecifiers = getThemeOrderSpecifiers(sortBy, direction);

        // 1. 테마 목록 페이징 처리
        List<ThemeResponse> pagedThemes = queryFactory
            .from(theme)
            .leftJoin(themeColor).on(themeColor.theme.id.eq(theme.id))
            .where(builder)
            .orderBy(orderSpecifiers.toArray(new OrderSpecifier<?>[0]))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .transform(
                GroupBy.groupBy(theme.id).list(
                    Projections.fields(
                        ThemeResponse.class,
                        theme.id.as("themeId"),
                        theme.name,
                        theme.color,
                        ExpressionUtils.as(Projections.list(themeColor.color), "chartColors"),
                        theme.createAt,
                        theme.modifyAt
                    )
                )
            );

        // 2. 각 테마에 대한 chartColors 리스트 조회 후 추가
        pagedThemes.forEach(themeResponse -> {
            List<String> chartColors = queryFactory
                .select(themeColor.color)
                .from(themeColor)
                .where(themeColor.theme.id.eq(themeResponse.getThemeId()))
                .fetch();
            themeResponse.setChartColors(chartColors); // chartColors 추가
        });

        // 3. 전체 테마 개수 계산
        long count = queryFactory
            .select(theme)
            .from(theme)
            .where(builder)
            .fetchCount();

        // 4. 페이지 결과 반환
        return new PageImpl<>(pagedThemes, pageable, count);
    }

    private List<OrderSpecifier<?>> getThemeOrderSpecifiers(String sortBy, String direction) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        if (sortBy != null) {
            Order order = direction.equalsIgnoreCase("asc") ? Order.ASC : Order.DESC;

            switch (sortBy) {
                case "id":
                    orderSpecifiers.add(new OrderSpecifier<>(order, theme.id));
                    break;
                case "modifyAt":
                    orderSpecifiers.add(new OrderSpecifier<>(order, theme.modifyAt));
                    break;
            }
        } else { //기본 정렬 기준
            orderSpecifiers.add(new OrderSpecifier<>(Order.ASC, theme.createAt));
        }
        return orderSpecifiers;
    }

    @Override
    public Page<ThemeResponse> findPaidThemes(PageUseCase useCase) {
        Pageable pageable = useCase.getPageable();
        String sortBy = useCase.getSortBy();
        String direction = useCase.getDirection();

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(paidTheme.isDeleted.eq(false));

        // 정렬 기준 추가
        List<OrderSpecifier<?>> orderSpecifiers = getPaidThemeOrderSpecifiers(sortBy, direction);

        // 1. 테마 목록 페이징 처리
        List<ThemeResponse> pagedThemes = queryFactory
            .from(paidTheme)
            .leftJoin(themeColor).on(themeColor.paidTheme.id.eq(paidTheme.id))
            .where(builder)
            .orderBy(orderSpecifiers.toArray(new OrderSpecifier<?>[0]))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .transform(
                GroupBy.groupBy(paidTheme.id).list(
                    Projections.fields(
                        ThemeResponse.class,
                        paidTheme.id.as("themeId"),
                        paidTheme.name,
                        paidTheme.color,
                        ExpressionUtils.as(Projections.list(themeColor.color), "chartColors"),
                        paidTheme.createAt,
                        paidTheme.modifyAt
                    )
                )
            );

        // 2. 각 테마에 대한 chartColors 리스트 조회 후 추가
        pagedThemes.forEach(themeResponse -> {
            List<String> chartColors = queryFactory
                .select(themeColor.color)
                .from(themeColor)
                .where(themeColor.paidTheme.id.eq(themeResponse.getThemeId()))
                .fetch();
            themeResponse.setChartColors(chartColors); // chartColors 추가
        });

        long count = queryFactory
            .select(paidTheme)
            .from(paidTheme)
            .where(builder)
            .fetchCount();

        return new PageImpl<>(pagedThemes, pageable, count);
    }

    private List<OrderSpecifier<?>> getPaidThemeOrderSpecifiers(String sortBy, String direction) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        if (sortBy != null) {
            Order order = direction.equalsIgnoreCase("asc") ? Order.ASC : Order.DESC;

            switch (sortBy) {
                case "id":
                    orderSpecifiers.add(new OrderSpecifier<>(order, paidTheme.id));
                    break;
                case "modifyAt":
                    orderSpecifiers.add(new OrderSpecifier<>(order, paidTheme.modifyAt));
                    break;
            }
        } else { //기본 정렬 기준
            orderSpecifiers.add(new OrderSpecifier<>(Order.ASC, paidTheme.createAt));
        }
        return orderSpecifiers;
    }


}
