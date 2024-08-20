package com.keypoint.keypointtravel.global.enumType.receipt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReceiptCategory {
    TRANSPORTATION(1, "교통비"),
    ACCOMMODATION(2, "숙박비"),
    SHOPPING(3, "쇼핑"),
    FOOD(4, "식비"),
    OTHER(5, "기타");

    private final int code;
    private final String description;
}

