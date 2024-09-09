package com.keypoint.keypointtravel.global.enumType.premium;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PremiumTitle {

    TWELVE_MONTHS("12개월", "tempValue");

    private final String title;
    private final String productId;
}
