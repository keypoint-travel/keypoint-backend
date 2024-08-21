package com.keypoint.keypointtravel.global.enumType.receipt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReceiptRegistrationType {
    PHOTO(1, "사진 촬영"),
    MANUAL(2, "수기 등록");

    private final int code;
    private final String description;
}
