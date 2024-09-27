package com.keypoint.keypointtravel.theme.repository;

import com.keypoint.keypointtravel.global.dto.useCase.PageUseCase;
import com.keypoint.keypointtravel.theme.dto.response.ThemeResponse;
import com.keypoint.keypointtravel.theme.entity.QPaidTheme;
import com.keypoint.keypointtravel.theme.entity.QTheme;
import com.keypoint.keypointtravel.theme.entity.QThemeColor;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.group.GroupBy;
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

        // Query 실행
        List<ThemeResponse> result = queryFactory
            .from(theme)
            .leftJoin(themeColor).on(themeColor.theme.id.eq(theme.id))
            .where(builder)
            .orderBy(orderSpecifiers.toArray(new OrderSpecifier<?>[0]))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .transform(
                GroupBy.groupBy(theme.id).list(
                    Projections.constructor(
                        ThemeResponse.class,
                        theme.id.as("themeId"),
                        theme.name,
                        theme.color,
                        GroupBy.list(themeColor.color),
                        theme.createAt,
                        theme.modifyAt
                    )
                )
            );

        long count = queryFactory
            .select(theme)
            .from(theme)
            .where(builder)
            .fetchCount();

        return new PageImpl<>(result, pageable, count);
    }

    private List<OrderSpecifier<?>> getThemeOrderSpecifiers(String sortBy, String direction) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        if (sortBy != null) {
            Order order = direction.equalsIgnoreCase("asc") ? Order.ASC : Order.DESC;

            switch (sortBy) {
                case "id":
                    orderSpecifiers.add(new OrderSpecifier<>(order, theme.id));
                    break;
            }
        } else { //기본 정렬 기준
            orderSpecifiers.add(new OrderSpecifier<>(Order.ASC, theme.modifyAt));
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

        // Query 실행
        List<ThemeResponse> result = queryFactory
            .from(paidTheme)
            .leftJoin(themeColor).on(themeColor.paidTheme.id.eq(paidTheme.id))
            .where(builder)
            .orderBy(orderSpecifiers.toArray(new OrderSpecifier<?>[0]))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .transform(
                GroupBy.groupBy(paidTheme.id).list(
                    Projections.constructor(
                        ThemeResponse.class,
                        paidTheme.id.as("themeId"),
                        paidTheme.name,
                        paidTheme.color,
                        GroupBy.list(themeColor.color),
                        paidTheme.createAt,
                        paidTheme.modifyAt
                    )
                )
            );

        long count = queryFactory
            .select(paidTheme)
            .from(paidTheme)
            .where(builder)
            .fetchCount();

        return new PageImpl<>(result, pageable, count);
    }

    private List<OrderSpecifier<?>> getPaidThemeOrderSpecifiers(String sortBy, String direction) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        if (sortBy != null) {
            Order order = direction.equalsIgnoreCase("asc") ? Order.ASC : Order.DESC;

            switch (sortBy) {
                case "id":
                    orderSpecifiers.add(new OrderSpecifier<>(order, paidTheme.id));
                    break;
            }
        } else { //기본 정렬 기준
            orderSpecifiers.add(new OrderSpecifier<>(Order.ASC, paidTheme.modifyAt));
        }
        return orderSpecifiers;
    }


}
