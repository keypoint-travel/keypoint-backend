package com.keypoint.keypointtravel.global.enumType.email;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EmailTemplate {
    EMAIL_VERIFICATION("01", "email-verification", "email-verification.title"),
    TRIP_END_REMINDER("02", "trip_end_reminder", ""),      // 여행 종료 알림
    SUGGEST_NEW_CAMPAIGN("03", "suggest_new_campaign", ""),    // 새로운 캠페인 제안
    INVITE_CAMPAIGN("04", "invite-campaign", "invite-campaign"),
    CAMPAIGN_REPORT("05", "campaign-report", "campaign-report"),
    COMMON_MARKETING_ALARM("06", "common-marketing-alarm", "common-marketing-alarm.title");

    private final String code;
    private final String template;
    private final String subject;
}
