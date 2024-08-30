package com.keypoint.keypointtravel.global.enumType.setting;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BadgeType {
    SIGN_UP("SIGN_UP", "회원가입 배지"),
    FIRST_CAMPAIGN("FIRST_CAMPAIGN", "첫 캠페인 완료 배지"),
    FRIEND_REGISTER("FRIEND_REGISTER", "친구 등록 배지"),
    NONE("NONE", "알 수 없음");

    private final String translatedKey;
    private final String description;
}
