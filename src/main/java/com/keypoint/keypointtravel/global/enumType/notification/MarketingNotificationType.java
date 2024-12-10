package com.keypoint.keypointtravel.global.enumType.notification;

import com.keypoint.keypointtravel.global.enumType.email.EmailTemplate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MarketingNotificationType {
    TRIP_END_REMINDER("여행 자동 종료 알림", EmailTemplate.TRIP_END_REMINDER),      // 여행 종료 알림
    SUGGEST_NEW_CAMPAIGN("새로운 캠페인 제안", EmailTemplate.SUGGEST_NEW_CAMPAIGN),    // 새로운 캠페인 제안
    ADMIN("어드민 마케팅 알림", EmailTemplate.COMMON_MARKETING_ALARM);

    private final String description;
    private final EmailTemplate template;
}
