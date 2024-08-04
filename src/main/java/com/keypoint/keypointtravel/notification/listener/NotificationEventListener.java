package com.keypoint.keypointtravel.notification.listener;

import com.keypoint.keypointtravel.global.utils.MessageSourceUtils;
import com.keypoint.keypointtravel.notification.event.PushNotificationEvent;
import java.util.Locale;
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
        String language1 = MessageSourceUtils.getLocalizedLanguage("pushNotification.friendInvite",
            Locale.ENGLISH);
        String language2 = MessageSourceUtils.getLocalizedLanguage("pushNotification.friendInvite",
            Locale.KOREAN);
        String language3 = MessageSourceUtils.getLocalizedLanguage("pushNotification.friendInvite",
            Locale.JAPANESE);
    }
}
