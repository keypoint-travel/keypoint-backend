package com.keypoint.keypointtravel.global.enumType.notification;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PushNotificationType {
    FRIEND_INVITE("친구 초대 알림", "pushNotification.friendInvite", "pushNotification.friendInvite"),
    CAMPAIGN_START("캠페인 시작 알림", "pushNotification.campaignStart", "pushNotification.campaignStart"),
    CAMPAIGN_END("캠페인 종료 알림", "pushNotification.campaignEnd", "pushNotification.campaignEnd"),
    CAMPAIGN_D_DAY("캠페인 D-1 알림", "pushNotification.campaignDday", "pushNotification.campaignDday"),
    CAMPAIGN_INVITE("캠페인 초대 알림", "pushNotification.campaignInvite",
        "pushNotification.campaignInvite"),
    CAMPAIGN_REGISTRATION("캠페인 등록 알림", "pushNotification.campaignRegistration",
        "pushNotification.campaignRegistration"),
    EVENT_NOTICE("이벤트/전체 공지 알림", "pushNotification.eventNotice", "pushNotification.eventNotice"),
    PAYMENT_COMPLETION("결제 완료 알림", "pushNotification.paymentCompletion",
        "pushNotification.paymentCompletion");

    private final String description;
    private final String titleLangCode;
    private final String contentLangCode;
}
