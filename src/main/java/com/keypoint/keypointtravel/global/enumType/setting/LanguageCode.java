package com.keypoint.keypointtravel.global.enumType.setting;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LanguageCode {
    EN("en", "영어"),
    KO("ko_KR", "한국어"),
    JA("ja_JP", "한국어");

    private final String code;
    private final String description;
}
