package com.keypoint.keypointtravel.global.enumType.email;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EmailTemplate {
    EMAIL_VERIFICATION("01", "email-verification", "이메일 인증"),
    TRIP_END_REMINDER("02", "trip_end_reminder", ""),      // 여행 종료 알림
    SUGGEST_NEW_CAMPAIGN("93", "suggest_new_campaign", ""),    // 새로운 캠페인 제안
    INVITE_CAMPAIGN("94", "invite-campaign", "캠페인 초대");


    private final String code;
    private final String template;
    private final String subject;
}
