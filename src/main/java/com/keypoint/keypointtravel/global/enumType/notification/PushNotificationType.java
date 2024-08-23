package com.keypoint.keypointtravel.global.enumType.notification;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PushNotificationType {
    RECEIPT_REGISTER("영수증 등록"),
    FRIEND_INVITE("친구 초대 알림"),
    CAMPAIGN_D_DAY("캠페인 D-1 알림"),
    CAMPAIGN_START("캠페인 시작 알림"),
    CAMPAIGN_END("캠페인 종료 알림"),
    CAMPAIGN_INVITE("캠페인 초대 알림"),
    CAMPAIGN_REGISTRATION("캠페인 등록 알림"),
    EVENT_NOTICE("이벤트/전체 공지 알림"),
    PAYMENT_COMPLETION("결제 완료 알림");

    private final String description;
}
