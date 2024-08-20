package com.keypoint.keypointtravel.global.enumType.receipt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReceiptCategory {
    EXCEPT(1, "기타");

    private final int code;
    private final String description;
}

