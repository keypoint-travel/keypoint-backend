package com.keypoint.keypointtravel.global.enumType.currency;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CurrencyType {

    KRW("KRW", "원"),
    JPY("JPY", "엔"),
    USD("USD", "달러");

    private final String code;
    private final String description;
}
