package com.keypoint.keypointtravel.notification.listener;

import com.keypoint.keypointtravel.notification.event.PushNotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationEventListener {

    @Async
    @TransactionalEventListener
    public void sendFCMNotification(PushNotificationEvent event) {
        log.info("Sending FCM notification event: {}", event);
    }
}
