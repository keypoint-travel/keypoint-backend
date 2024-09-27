package com.keypoint.keypointtravel.theme.repository;

import com.keypoint.keypointtravel.member.entity.QMemberDetail;
import com.keypoint.keypointtravel.theme.dto.response.MemberThemeResponse;
import com.keypoint.keypointtravel.theme.entity.QPaidTheme;
import com.keypoint.keypointtravel.theme.entity.QTheme;
import com.keypoint.keypointtravel.theme.entity.QThemeColor;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class MemberThemeCustomRepositoryImpl  implements MemberThemeCustomRepository {

    private final JPAQueryFactory queryFactory;
    private final QTheme theme = QTheme.theme;
    private final QPaidTheme paidTheme = QPaidTheme.paidTheme;
    private final QMemberDetail memberDetail = QMemberDetail.memberDetail;

    private final QThemeColor themeColor = QThemeColor.themeColor;



    @Override
    public MemberThemeResponse findThemeByUserId(Long memberId) {
        MemberThemeResponse response = queryFactory
            .select(
                Projections.fields(
                    MemberThemeResponse.class,
                    new CaseBuilder()
                        .when(paidTheme.isNotNull())
                        .then(paidTheme.color)
                        .otherwise(theme.color).as("color"),
                    new CaseBuilder()
                        .when(paidTheme.isNotNull())
                        .then(paidTheme.name)
                        .otherwise(theme.name).as("name")
                )
            )
            .from(memberDetail)
            .leftJoin(paidTheme).on(memberDetail.paidTheme.eq(paidTheme))
            .leftJoin(theme).on(memberDetail.theme.eq(theme))
            .where(memberDetail.member.id.eq(memberId))
            .fetchFirst();

        // chartColors 가져오기
        List<String> chartColors = queryFactory
            .select(themeColor.color)
            .from(themeColor)
            .leftJoin(paidTheme).on(memberDetail.paidTheme.eq(paidTheme))
            .leftJoin(theme).on(memberDetail.theme.eq(theme))
            .where(
                memberDetail.paidTheme.isNotNull()
                    .and(themeColor.paidTheme.eq(paidTheme))
                    .or(memberDetail.paidTheme.isNull()
                        .and(themeColor.theme.eq(theme)))
            )
            .fetch();

        return response != null ? response.withChartColors(chartColors) : null;
    }





}
