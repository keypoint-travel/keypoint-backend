package com.keypoint.keypointtravel.requestPush.repository;

import com.keypoint.keypointtravel.global.dto.useCase.PageUseCase;
import com.keypoint.keypointtravel.global.enumType.member.RoleType;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.requestPush.dto.response.RequestPushAlarmResponse;
import com.keypoint.keypointtravel.requestPush.dto.useCase.UpdateRequestPushAlarmUseCase;
import com.keypoint.keypointtravel.requestPush.entity.QRequestPush;
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

    private final QRequestPush requestPush = QRequestPush.requestPush;

    @Override
    public long updateRequestPushAlarm(UpdateRequestPushAlarmUseCase useCase) {
        String currentAuditor = auditorProvider.getCurrentAuditor().orElse(null);

        return queryFactory.update(requestPush)
            .set(requestPush.title, useCase.getTitle())
            .set(requestPush.content, useCase.getContent())
            .set(requestPush.languageCode, useCase.getLanguageCode())
            .set(requestPush.reservationAt, useCase.getReservationAt())
            .set(requestPush.roleType, useCase.getRoleType())
            .set(requestPush.modifyAt, LocalDateTime.now())
            .set(requestPush.modifyId, currentAuditor)
            .where(requestPush.id.eq(useCase.getRequestPushId()))
            .execute();
    }

    @Override
    public Page<RequestPushAlarmResponse> findRequestPushAlarms(PageUseCase useCase) {
        Pageable pageable = useCase.getPageable();
        String sortBy = useCase.getSortBy();
        String direction = useCase.getDirection();

        // 기본 정렬 기준 추가
        OrderSpecifier<?> orderSpecifier = new OrderSpecifier<>(Order.ASC, requestPush.modifyAt);

        // 동적 정렬 기준 처리
        if (sortBy != null) {
            Order order = direction.equals("asc") ? Order.ASC : Order.DESC;

            switch (sortBy) {
                case "requestId":
                    orderSpecifier = new OrderSpecifier<>(order, requestPush.registerId);
                    break;
                case "roleType":
                    if (order == Order.ASC) {
                        orderSpecifier = RoleType.getNumberExpression(requestPush.roleType)
                            .asc();
                    } else {
                        orderSpecifier = RoleType.getNumberExpression(requestPush.roleType)
                            .desc();
                    }
                    break;
                case "languageCode":
                    if (order == Order.ASC) {
                        orderSpecifier = LanguageCode.getNumberExpression(requestPush.languageCode)
                            .asc();
                    } else {
                        orderSpecifier = LanguageCode.getNumberExpression(requestPush.languageCode)
                            .desc();
                    }
                    break;
                case "title":
                    orderSpecifier = new OrderSpecifier<>(order, requestPush.title);
                    break;
                case "content":
                    orderSpecifier = new OrderSpecifier<>(order, requestPush.content);
                    break;
                case "reservationAt":
                case "sendAt":
                    orderSpecifier = new OrderSpecifier<>(order, requestPush.reservationAt);
                    break;
            }
        }

        List<RequestPushAlarmResponse> data = queryFactory.select(
                Projections.constructor(
                    RequestPushAlarmResponse.class,
                    requestPush.id,
                    requestPush.roleType,
                    requestPush.languageCode,
                    requestPush.title,
                    requestPush.content,
                    requestPush.reservationAt
                )
            )
            .from(requestPush)
            .orderBy(orderSpecifier)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long count = queryFactory
            .select(
                requestPush.count()
            )
            .from(requestPush)
            .fetchFirst();

        return new PageImpl<>(data, pageable, count);
    }
}
