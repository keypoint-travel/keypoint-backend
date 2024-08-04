package com.keypoint.keypointtravel.global.enumType.setting;

import java.util.Locale;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LanguageCode {
    EN("en", "영어", Locale.ENGLISH),
    KO("ko_KR", "한국어", Locale.KOREA),
    JA("ja_JP", "일본어", Locale.JAPANESE);

    private final String code;
    private final String description;
    private final Locale locale;
}
