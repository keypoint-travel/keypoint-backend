package com.keypoint.keypointtravel.global.enumType.notification;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PushNotificationType {
    RECEIPT_REGISTER("영수증 등록"),
    CAMPAIGN_D_MINUS_7("캠페인 D-7"),
    CAMPAIGN_D_DAY("캠페인 D-1 알림"),
    CAMPAIGN_INVITE("캠페인 초대 알림"),
    CAMPAIGN_ACCEPT_INVITER("캠페인 수락 (팀원이 수락을 했다는 것을 방장이 받는 알림.)"),
    CAMPAIGN_ACCEPT_INVITEE("캠페인 수락 (방장이 수락했다는 것을 팀원이 받는 알림)"),
    CAMPAIGN_NO_EXPENSE_D1("캠페인 D+1 지출 내역이 없는 경우"),
    CAMPAIGN_END("캠페인 종료 알림"),
    CAMPAIGN_JOIN_REQUEST("캠페인 참여 신청 (신청 받은 사람-방장)"),
    PAYMENT_COMPLETION("결제 완료 알림"),
    FRIEND_ADDED("친구 추가 (친추 받은 사람)"),
    FRIEND_ACCEPTED_RECEIVER("친구 수락 (친추 받은 사람)"),
    FRIEND_ACCEPTED_SENDER("친구 수락 (친추 보낸 사람)"),
    INQUIRY_RESPONSE_COMPLETED("1:1 문의 답변 완료"),
    CAMPAIGN_D60_PASSED("마지막 캠페인 D+60일 경과"),

    CAMPAIGN_START("캠페인 시작 알림"),
    EVENT_NOTICE("이벤트/전체 공지 알림");

    private final String description;
}
