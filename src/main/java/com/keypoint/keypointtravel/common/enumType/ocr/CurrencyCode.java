package com.keypoint.keypointtravel.common.enumType.ocr;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CurrencyCode {
    NONE(null, "알 수 없음"),
    KRW("원", "원"),
    JPY("¥", "엔"),
    USD("$", "미국 달러"),
    EUR("€", "유로");

    private final String symbol;
    private final String currency;

    public static CurrencyCode fromContent(String content) {
        if (content == null) {
            return CurrencyCode.NONE;
        }

        for (CurrencyCode code : CurrencyCode.values()) {
            if (code.getSymbol() != null && content.contains(code.getSymbol())) {
                return code;
            }
        }
        return CurrencyCode.NONE;
    }
}
