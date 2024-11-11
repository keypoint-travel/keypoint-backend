package com.keypoint.keypointtravel.member.repository.memberVisitors;

import com.keypoint.keypointtravel.member.dto.response.StatisticResponse;
import com.keypoint.keypointtravel.member.entity.QMemberVisitors;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;

@RequiredArgsConstructor
public class MemberVisitorsCustomRepositoryImpl implements MemberVisitorsCustomRepository {

    private final JPAQueryFactory queryFactory;
    private final AuditorAware<String> auditorProvider;

    private final QMemberVisitors memberVisitors = QMemberVisitors.memberVisitors;

    @Override
    public List<StatisticResponse> findDailyVisitorsStatistics(
        LocalDate startAt,
        LocalDate endAt
    ) {
        return queryFactory
            .select(
                Projections.fields(
                    StatisticResponse.class,
                    Expressions.stringTemplate(
                        "DATE_FORMAT({0}, {1})",
                        memberVisitors.date,
                        Expressions.constant("%Y/%m/%d")).as("date"),
                    memberVisitors.visitors.as("value")
                )
            )
            .from(memberVisitors)
            .where(memberVisitors.date.isNotNull()
                .and(memberVisitors.date.goe(startAt))
                .and(memberVisitors.date.lt(endAt))
            )
            .orderBy(memberVisitors.date.asc()).fetch();
    }
}
