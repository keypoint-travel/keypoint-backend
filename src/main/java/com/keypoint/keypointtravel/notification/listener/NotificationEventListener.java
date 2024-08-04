package com.keypoint.keypointtravel.notification.listener;

import com.keypoint.keypointtravel.notification.event.pushNotification.PushNotificationEvent;
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
        switch (event.getPushNotificationType()) {
            case FRIEND_INVITE -> {

            }
            case CAMPAIGN_START -> {

            }
            case CAMPAIGN_END -> {

            }
            case CAMPAIGN_D_DAY -> {

            }
            case CAMPAIGN_INVITE -> {

            }
            case CAMPAIGN_REGISTRATION -> {

            }
            case EVENT_NOTICE -> {

            }
            case PAYMENT_COMPLETION -> {

            }
        }
    }
}
