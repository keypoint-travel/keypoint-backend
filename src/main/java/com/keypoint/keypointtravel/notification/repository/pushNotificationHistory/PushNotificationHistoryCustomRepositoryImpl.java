package com.keypoint.keypointtravel.notification.repository.pushNotificationHistory;

import com.keypoint.keypointtravel.notification.entity.QPushNotificationHistory;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PushNotificationHistoryCustomRepositoryImpl implements
    PushNotificationHistoryCustomRepository {

    private final JPAQueryFactory queryFactory;

    private final QPushNotificationHistory pushNotificationHistory = QPushNotificationHistory.pushNotificationHistory;


    @Override
    public boolean existsByIsReadFalseAndMemberId(Long memberId) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(pushNotificationHistory.isRead.eq(false))
            .and(pushNotificationHistory.member.id.eq(memberId));

        return queryFactory.selectFrom(pushNotificationHistory)
            .where(booleanBuilder)
            .fetchOne() != null;
    }
}
