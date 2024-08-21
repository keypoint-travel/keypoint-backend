package com.keypoint.keypointtravel.notification.repository.notification;

import com.keypoint.keypointtravel.global.enumType.notification.AlarmType;
import com.keypoint.keypointtravel.notification.dto.useCase.UpdateNotificationUseCase;
import com.keypoint.keypointtravel.notification.entity.QNotification;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;

@RequiredArgsConstructor
public class NotificationCustomRepositoryImpl implements NotificationCustomRepository {

    private final JPAQueryFactory queryFactory;
    private final AuditorAware<String> auditorProvider;

    private final QNotification notification = QNotification.notification;

    @Override
    public void updateNotification(UpdateNotificationUseCase useCase) {
        String currentAuditor = auditorProvider.getCurrentAuditor().orElse(null);

        JPAUpdateClause query = queryFactory.update(notification)
            .set(notification.modifyAt, LocalDateTime.now())
            .set(notification.modifyId, currentAuditor)
            .where(notification.member.id.eq(useCase.getMemberId()));

        AlarmType alarmType = useCase.getAlarmType();
        boolean changedValue = useCase.isNotificationEnabled();
        if (alarmType == AlarmType.PUSH) {
            query.set(notification.pushNotificationEnabled, changedValue)
                .execute();
        } else if (alarmType == AlarmType.MARKETING) {
            query.set(notification.marketingNotificationEnabled, changedValue)
                .execute();
        }
    }
}
