package com.keypoint.keypointtravel.notification.repository.pushNotificationHistory;

import com.keypoint.keypointtravel.notification.dto.useCase.CommonPushHistoryUseCase;
import com.keypoint.keypointtravel.notification.entity.QPushNotificationHistory;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
        List<CommonPushHistoryUseCase> result = queryFactory
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
            .limit(pageable.getPageSize() + 1)
            .fetch();

        return result;
    }
}
