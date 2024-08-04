package com.keypoint.keypointtravel.notification.service;

import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationType;
import com.keypoint.keypointtravel.notification.event.pushNotification.CampaignInvitePushNotificationEvent;
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
        eventPublisher.publishEvent(CampaignInvitePushNotificationEvent.of(
            PushNotificationType.FRIEND_INVITE,
            List.of(5L),
            "캠페인명",
            "초대자"
        ));
    }
}
