package com.keypoint.keypointtravel.global.enumType.setting;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Locale;

@Getter
@RequiredArgsConstructor
public enum LanguageCode {
    EN("en-US", "영어", Locale.ENGLISH),
    KO("ko-KR", "한국어", Locale.KOREA),
    JA("ja", "일본어", Locale.JAPANESE);

    private final String code;
    private final String description;
    private final Locale locale;
}
