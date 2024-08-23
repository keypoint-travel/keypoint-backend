package com.keypoint.keypointtravel.notification.service;

import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationType;
import com.keypoint.keypointtravel.notification.event.pushNotification.CampaignInvitePushNotificationEvent;
import com.keypoint.keypointtravel.notification.event.pushNotification.CampaignPushNotificationEvent;
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
            List.of(1L),
            "여름 방학",
            "홍길동"
        ));
        eventPublisher.publishEvent(CampaignInvitePushNotificationEvent.of(
            PushNotificationType.FRIEND_INVITE,
            List.of(1L),
            "여름 휴가 - 강릉",
            "홍길동"
        ));
        eventPublisher.publishEvent(CampaignPushNotificationEvent.of(
            PushNotificationType.CAMPAIGN_START,
            List.of(1L),
            "여름 방학"
        ));
        eventPublisher.publishEvent(CampaignPushNotificationEvent.of(
            PushNotificationType.CAMPAIGN_START,
            List.of(1L),
            "여름 휴가 - 강릉"
        ));
    }
}
