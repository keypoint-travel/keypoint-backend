package com.keypoint.keypointtravel.notification.repository.pushNotificationHistory;

import com.keypoint.keypointtravel.global.dto.useCase.SearchPageAndMemberIdUseCase;
import com.keypoint.keypointtravel.notification.dto.useCase.CommonPushHistoryUseCase;
import com.keypoint.keypointtravel.notification.entity.QPushNotificationHistory;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class PushNotificationHistoryCustomRepositoryImpl implements
    PushNotificationHistoryCustomRepository {

    private final JPAQueryFactory queryFactory;
    private final AuditorAware<String> auditorProvider;

    private final QPushNotificationHistory pushNotificationHistory = QPushNotificationHistory.pushNotificationHistory;


    @Override
    public boolean existsByIsReadFalseAndMemberId(Long memberId) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(pushNotificationHistory.isRead.eq(false))
            .and(pushNotificationHistory.member.id.eq(memberId));

        Integer result = queryFactory.selectOne()
            .from(pushNotificationHistory)
            .where(booleanBuilder)
            .fetchFirst(); // 첫 번째 결과만 조회하고, 없으면 null 반환

        return result != null;
    }

    @Override
    public List<CommonPushHistoryUseCase> findPushHistories(Long memberId, Pageable pageable) {
        // 1. 푸시 이력 조회
        return queryFactory
            .select(
                Projections.fields(
                    CommonPushHistoryUseCase.class,
                    pushNotificationHistory.id.as("historyId"),
                    pushNotificationHistory.type,
                    pushNotificationHistory.detailData,
                    pushNotificationHistory.createAt.as("arrivedAt"),
                    pushNotificationHistory.isRead
                )
            )
            .from(pushNotificationHistory)
            .where(pushNotificationHistory.member.id.eq(memberId))
            .orderBy(pushNotificationHistory.createAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
    }

    @Override
    public List<CommonPushHistoryUseCase> findPushHistoriesInWeb(
        SearchPageAndMemberIdUseCase useCase
    ) {
        Pageable pageable = useCase.getPageable();
        String sortBy = useCase.getSortBy();
        String direction = useCase.getDirection();
        String keyword = useCase.getKeyword();

        // 동적 정렬 기준 생성
        List<OrderSpecifier<?>> orderSpecifiers = getPushHistoriesOrderSpecifiers(sortBy,
            direction);

        // 검색어 조회 조건 추가
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (keyword != null && !keyword.isBlank()) {
            booleanBuilder.and(pushNotificationHistory.member.name.containsIgnoreCase(keyword));
        }

        // 푸시 이력 조회
        return queryFactory
            .select(
                Projections.fields(
                    CommonPushHistoryUseCase.class,
                    pushNotificationHistory.id.as("historyId"),
                    pushNotificationHistory.type,
                    pushNotificationHistory.detailData,
                    pushNotificationHistory.createAt.as("arrivedAt"),
                    pushNotificationHistory.isRead,
                    pushNotificationHistory.member.id.as("memberId")
                )
            )
            .from(pushNotificationHistory)
            .where(booleanBuilder)
            .orderBy(orderSpecifiers.toArray(new OrderSpecifier<?>[0]))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
    }

    private List<OrderSpecifier<?>> getPushHistoriesOrderSpecifiers(String sortBy,
        String direction) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        if (sortBy != null) {
            Order order = direction.equalsIgnoreCase("asc") ? Order.ASC : Order.DESC;

            switch (sortBy) {
                case "historyId":
                    orderSpecifiers.add(new OrderSpecifier<>(order, pushNotificationHistory.id));
                    break;
                case "title":
                    orderSpecifiers.add(new OrderSpecifier<>(order, pushNotificationHistory.type));
                    break;
                case "userName":
                    orderSpecifiers.add(
                        new OrderSpecifier<>(order, pushNotificationHistory.member.name)
                    );
                    break;
                case "createAt":
                    orderSpecifiers.add(
                        new OrderSpecifier<>(order, pushNotificationHistory.createAt));
                    break;
            }
        } else { //기본 정렬 기준
            orderSpecifiers.add(new OrderSpecifier<>(Order.ASC, pushNotificationHistory.createAt));
        }
        return orderSpecifiers;
    }

    @Override
    public long countPushHistoriesInWeb(String keyword) {
        // 검색어 조회 조건 추가
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (keyword != null && !keyword.isBlank()) {
            booleanBuilder.and(pushNotificationHistory.member.name.containsIgnoreCase(keyword));
        }

        // 조회
        return queryFactory
            .select(
                pushNotificationHistory.count()
            )
            .from(pushNotificationHistory)
            .where(booleanBuilder)
            .fetchOne();
    }

    @Override
    public long countPushHistories(Long memberId) {
        return queryFactory
            .select(
                pushNotificationHistory.count()
            )
            .from(pushNotificationHistory)
            .where(pushNotificationHistory.member.id.eq(memberId))
            .fetchOne();
    }
}
