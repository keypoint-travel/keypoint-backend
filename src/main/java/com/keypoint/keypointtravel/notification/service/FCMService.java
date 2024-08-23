package com.keypoint.keypointtravel.notification.service;

import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationType;
import com.keypoint.keypointtravel.notification.event.pushNotification.CommonPushNotificationEvent;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FCMService {

    private final ApplicationEventPublisher eventPublisher;

    public void testEvent() {
        eventPublisher.publishEvent(CommonPushNotificationEvent.of(
            PushNotificationType.RECEIPT_REGISTER,
            List.of(5L)
        ));
        eventPublisher.publishEvent(CommonPushNotificationEvent.of(
            PushNotificationType.RECEIPT_REGISTER,
            List.of(5L)
        ));
        eventPublisher.publishEvent(CommonPushNotificationEvent.of(
            PushNotificationType.RECEIPT_REGISTER,
            List.of(5L)
        ));
        eventPublisher.publishEvent(CommonPushNotificationEvent.of(
            PushNotificationType.RECEIPT_REGISTER,
            List.of(5L)
        ));
    }
}
