package com.keypoint.keypointtravel.requestPush.repository;

import com.keypoint.keypointtravel.global.dto.useCase.PageUseCase;
import com.keypoint.keypointtravel.global.enumType.member.RoleType;
import com.keypoint.keypointtravel.global.enumType.notification.RequestAlarmType;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.requestPush.dto.response.RequestPushAlarmResponse;
import com.keypoint.keypointtravel.requestPush.dto.useCase.UpdateRequestPushAlarmUseCase;
import com.keypoint.keypointtravel.requestPush.entity.QRequestAlarm;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class RequestPushCustomRepositoryImpl implements RequestPushCustomRepository {

    private final JPAQueryFactory queryFactory;
    private final AuditorAware<String> auditorProvider;

    private final QRequestAlarm requestAlarm = QRequestAlarm.requestAlarm;

    @Override
    public long updateRequestPushAlarm(UpdateRequestPushAlarmUseCase useCase) {
        String currentAuditor = auditorProvider.getCurrentAuditor().orElse(null);

        return queryFactory.update(requestAlarm)
            .set(requestAlarm.title, useCase.getTitle())
            .set(requestAlarm.content, useCase.getContent())
            .set(requestAlarm.languageCode, useCase.getLanguageCode())
            .set(requestAlarm.reservationAt, useCase.getReservationAt())
            .set(requestAlarm.roleType, useCase.getRoleType())
            .set(requestAlarm.modifyAt, LocalDateTime.now())
            .set(requestAlarm.modifyId, currentAuditor)
            .where(requestAlarm.id.eq(useCase.getRequestPushId()))
            .execute();
    }

    @Override
    public Page<RequestPushAlarmResponse> findRequestPushAlarms(PageUseCase useCase) {
        Pageable pageable = useCase.getPageable();
        String sortBy = useCase.getSortBy();
        String direction = useCase.getDirection();

        // 필터링
        BooleanBuilder booleanBuilder = new BooleanBuilder()
            .and(requestAlarm.type.eq(RequestAlarmType.PUSH));

        // 기본 정렬 기준 추가
        OrderSpecifier<?> orderSpecifier = new OrderSpecifier<>(Order.ASC, requestAlarm.modifyAt);

        // 동적 정렬 기준 처리
        if (sortBy != null) {
            Order order = direction.equals("asc") ? Order.ASC : Order.DESC;

            switch (sortBy) {
                case "requestId":
                    orderSpecifier = new OrderSpecifier<>(order, requestAlarm.registerId);
                    break;
                case "roleType":
                    if (order == Order.ASC) {
                        orderSpecifier = RoleType.getNumberExpression(requestAlarm.roleType)
                            .asc();
                    } else {
                        orderSpecifier = RoleType.getNumberExpression(requestAlarm.roleType)
                            .desc();
                    }
                    break;
                case "languageCode":
                    if (order == Order.ASC) {
                        orderSpecifier = LanguageCode.getNumberExpression(requestAlarm.languageCode)
                            .asc();
                    } else {
                        orderSpecifier = LanguageCode.getNumberExpression(requestAlarm.languageCode)
                            .desc();
                    }
                    break;
                case "title":
                    orderSpecifier = new OrderSpecifier<>(order, requestAlarm.title);
                    break;
                case "content":
                    orderSpecifier = new OrderSpecifier<>(order, requestAlarm.content);
                    break;
                case "reservationAt":
                case "sendAt":
                    orderSpecifier = new OrderSpecifier<>(order, requestAlarm.reservationAt);
                    break;
            }
        }

        List<RequestPushAlarmResponse> data = queryFactory.select(
                Projections.constructor(
                    RequestPushAlarmResponse.class,
                    requestAlarm.id,
                    requestAlarm.roleType,
                    requestAlarm.languageCode,
                    requestAlarm.title,
                    requestAlarm.content,
                    requestAlarm.reservationAt
                )
            )
            .from(requestAlarm)
            .where(booleanBuilder)
            .orderBy(orderSpecifier)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long count = queryFactory
            .select(
                requestAlarm.count()
            )
            .from(requestAlarm)
            .where(booleanBuilder)
            .fetchFirst();

        return new PageImpl<>(data, pageable, count);
    }
}
