package com.keypoint.keypointtravel.global.enumType.setting;

import com.keypoint.keypointtravel.global.utils.LogUtils;
import java.util.Locale;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LanguageCode {
    EN("en-US", "영어", Locale.ENGLISH),
    KO("ko-KR", "한국어", Locale.KOREA),
    JA("ja", "일본어", Locale.JAPANESE);

    private final String code;
    private final String description;
    private final Locale locale;

    public static LanguageCode fromCode(String code) {
        for (LanguageCode languageCode : LanguageCode.values()) {
            if (languageCode.getCode().equalsIgnoreCase(code)) {
                return languageCode;
            }
        }

        LogUtils.writeErrorLog("LanguageCode, fromCode",
            "Failed to find language code for language code " + code);
        return LanguageCode.EN;
    }
}
