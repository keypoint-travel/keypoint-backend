package com.keypoint.keypointtravel.notification.repository.pushNotificationHistory;

import com.keypoint.keypointtravel.notification.dto.response.PushHistoryResponse;
import com.keypoint.keypointtravel.notification.entity.QPushNotificationHistory;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

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

    @Override
    public Slice<PushHistoryResponse> findPushHistories(Long memberId, Pageable pageable) {
        List<PushHistoryResponse> result = queryFactory
            .select(
                Projections.fields(
                    PushHistoryResponse.class,
                    pushNotificationHistory.id.as("historyId"),
                    pushNotificationHistory.title,
                    pushNotificationHistory.content,
                    pushNotificationHistory.createAt.as("arrivedAt")
                )
            )
            .from(pushNotificationHistory)
            .where(pushNotificationHistory.member.id.eq(memberId))
            .orderBy(pushNotificationHistory.createAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize() + 1)
            .fetch();

        boolean hasNext = false;
        if (result.size() > pageable.getPageSize()) {
            hasNext = true;
            result.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(result, pageable, hasNext);
    }
}
