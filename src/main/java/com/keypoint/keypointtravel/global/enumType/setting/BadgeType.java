package com.keypoint.keypointtravel.global.enumType.setting;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BadgeType {
    SIGN_UP(
            "SIGN_UP",
            0,
            "badge.sign_up",
            "회원가입 배지"
    ),
    FIRST_CAMPAIGN(

            "FIRST_CAMPAIGN",
            1,
            "badge.first_campaign",
            "첫 캠페인 완료 배지"
    ),
    FRIEND_REGISTER(

            "FRIEND_REGISTER",
            2,
            "badge.friend_register",
            "친구 등록 배지"
    ),
    NONE(
            "NONE",
            100,
            "",
            "알 수 없음"
    );

    private final String translatedKey;
    private final int defaultOrder;
    private final String languageKey;
    private final String description;
}
