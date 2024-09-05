package com.keypoint.keypointtravel.global.enumType.premium;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PremiumTitle {

    TWELVE_MONTHS("12개월", "tempValue");

    private final String title;
    private final String productId;
}
