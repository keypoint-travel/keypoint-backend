package com.keypoint.keypointtravel.global.enumType.notification;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PushNotificationMsg {
    RECEIPT_REGISTER(
        PushNotificationType.RECEIPT_REGISTER,
        "pushNotification.receiptRegister_title",
        "pushNotification.receiptRegister_content"
    ),

    FRIEND_INVITE_1(
        PushNotificationType.FRIEND_INVITE,
        "pushNotification.friendInvite1_title",
        "pushNotification.friendInvite1_content"
    ),
    FRIEND_INVITE_2(
        PushNotificationType.FRIEND_INVITE,
        "pushNotification.friendInvite2_title",
        "pushNotification.friendInvite2_content"
    ),
    FRIEND_INVITE_3(
        PushNotificationType.FRIEND_INVITE,
        "pushNotification.friendInvite3_title",
        "pushNotification.friendInvite3_content"
    ),
    FRIEND_INVITE_4(
        PushNotificationType.FRIEND_INVITE,
        "pushNotification.friendInvite4_title",
        "pushNotification.friendInvite4_content"
    ),
    FRIEND_INVITE_5(
        PushNotificationType.FRIEND_INVITE,
        "pushNotification.friendInvite5_title",
        "pushNotification.friendInvite5_content"
    ),

    CAMPAIGN_D_DAY_1(
        PushNotificationType.CAMPAIGN_D_DAY,
        "pushNotification.campaignDDay1_title",
        "pushNotification.campaignDDay1_content"
    ),
    CAMPAIGN_D_DAY_2(
        PushNotificationType.CAMPAIGN_D_DAY,
        "pushNotification.campaignDDay2_title",
        "pushNotification.campaignDDay2_content"
    ),
    CAMPAIGN_D_DAY_3(
        PushNotificationType.CAMPAIGN_D_DAY,
        "pushNotification.campaignDDay3_title",
        "pushNotification.campaignDDay3_content"
    ),
    CAMPAIGN_D_DAY_4(
        PushNotificationType.CAMPAIGN_D_DAY,
        "pushNotification.campaignDDay4_title",
        "pushNotification.campaignDDay4_content"
    ),
    CAMPAIGN_D_DAY_5(
        PushNotificationType.CAMPAIGN_D_DAY,
        "pushNotification.campaignDDay5_title",
        "pushNotification.campaignDDay5_content"
    ),

    CAMPAIGN_START(
        PushNotificationType.CAMPAIGN_START, "pushNotification.campaignStart",
        "pushNotification.campaignStart"),

    CAMPAIGN_END("캠페인 종료 알림", "pushNotification.campaignEnd", "pushNotification.campaignEnd"),

    CAMPAIGN_INVITE("캠페인 초대 알림", "pushNotification.campaignInvite",
        "pushNotification.campaignInvite"),
    CAMPAIGN_REGISTRATION("캠페인 등록 알림", "pushNotification.campaignRegistration",
        "pushNotification.campaignRegistration"),
    EVENT_NOTICE("이벤트/전체 공지 알림", "pushNotification.eventNotice", "pushNotification.eventNotice"),
    PAYMENT_COMPLETION("결제 완료 알림", "pushNotification.paymentCompletion",
        "pushNotification.paymentCompletion");

    private final PushNotificationType type;
    private final String titleLangCode;
    private final String contentLangCode;
}
