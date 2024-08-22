package com.keypoint.keypointtravel.notification.service;

import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationType;
import com.keypoint.keypointtravel.notification.event.pushNotification.CampaignAcceptorPushNotificationEvent;
import com.keypoint.keypointtravel.notification.event.pushNotification.CampaignApplicantPushNotificationEvent;
import com.keypoint.keypointtravel.notification.event.pushNotification.CampaignLeaderPushNotificationEvent;
import com.keypoint.keypointtravel.notification.event.pushNotification.CampaignPushNotificationEvent;
import com.keypoint.keypointtravel.notification.event.pushNotification.CommonPushNotificationEvent;
import com.keypoint.keypointtravel.notification.event.pushNotification.FriendPushNotificationEvent;
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
            PushNotificationType.CAMPAIGN_D_MINUS_7,
            List.of(5L)
        ));
        eventPublisher.publishEvent(CommonPushNotificationEvent.of(
            PushNotificationType.CAMPAIGN_D_DAY,
            List.of(5L)
        ));
        eventPublisher.publishEvent(CommonPushNotificationEvent.of(
            PushNotificationType.CAMPAIGN_NO_EXPENSE_D1,
            List.of(5L)
        ));
        eventPublisher.publishEvent(CommonPushNotificationEvent.of(
            PushNotificationType.PAYMENT_COMPLETION,
            List.of(5L)
        ));
        eventPublisher.publishEvent(CommonPushNotificationEvent.of(
            PushNotificationType.INQUIRY_RESPONSE_COMPLETED,
            List.of(5L)
        ));
        eventPublisher.publishEvent(CommonPushNotificationEvent.of(
            PushNotificationType.CAMPAIGN_D60_PASSED,
            List.of(5L)
        ));

        eventPublisher.publishEvent(CampaignLeaderPushNotificationEvent.of(
            PushNotificationType.CAMPAIGN_INVITE,
            List.of(5L),
            "방장1",
            1L
        ));
        eventPublisher.publishEvent(CampaignLeaderPushNotificationEvent.of(
            PushNotificationType.CAMPAIGN_ACCEPT_INVITEE,
            List.of(5L),
            "방장2",
            1L
        ));

        eventPublisher.publishEvent(CampaignAcceptorPushNotificationEvent.of(
            PushNotificationType.CAMPAIGN_ACCEPT_INVITER,
            List.of(5L),
            "승락한 사람",
            1L
        ));

        eventPublisher.publishEvent(CampaignPushNotificationEvent.of(
            PushNotificationType.CAMPAIGN_END,
            List.of(5L),
            1L
        ));

        eventPublisher.publishEvent(CampaignApplicantPushNotificationEvent.of(
            PushNotificationType.CAMPAIGN_JOIN_REQUEST,
            List.of(5L),
            "신청자",
            1L
        ));

        eventPublisher.publishEvent(FriendPushNotificationEvent.of(
            PushNotificationType.FRIEND_ADDED,
            List.of(5L),
            "도레"
        ));
        eventPublisher.publishEvent(FriendPushNotificationEvent.of(
            PushNotificationType.FRIEND_ACCEPTED_RECEIVER,
            List.of(5L),
            "미파"
        ));
        eventPublisher.publishEvent(FriendPushNotificationEvent.of(
            PushNotificationType.FRIEND_ACCEPTED_SENDER,
            List.of(5L),
            "솔라"
        ));
    }
}
