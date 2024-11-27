package com.keypoint.keypointtravel.global.enumType.setting;

import com.keypoint.keypointtravel.global.utils.LogUtils;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import java.util.Locale;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LanguageCode {
    EN("en-US", "en", "영어", Locale.ENGLISH),
    KO("ko-KR", "ko", "한국어", Locale.KOREA),
    JA("ja", "ja", "일본어", Locale.JAPANESE);

    private final String code;
    private final String countryCode;
    private final String description;
    private final Locale locale;

    public static LanguageCode fromCode(String code) {
        for (LanguageCode languageCode : LanguageCode.values()) {
            if (code.contains(languageCode.getCountryCode()) || languageCode.getCountryCode()
                .contains(code)) {
                return languageCode;
            }
        }

        LogUtils.writeErrorLog("LanguageCode, fromCode",
            "Failed to find language code for language code " + code);
        return LanguageCode.EN;
    }

    public static NumberExpression<Integer> getNumberExpression(EnumPath<LanguageCode> path) {
        return Expressions.cases()
            .when(path.eq(LanguageCode.EN)).then(1)
            .when(path.eq(LanguageCode.JA)).then(2)
            .when(path.eq(LanguageCode.KO)).then(3)
            .otherwise(4);
    }
}
