package com.keypoint.keypointtravel.common.enumType.ocr;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OCRFieldType {
    ADDRESS("address"),
    STRING("string"),
    PHONE_NUMBER("phoneNumber"),
    NUMBER("number"),
    DATE("date"),
    TIME("time"),
    CURRENCY("currency"),
    OBJECT("object"),
    ARRAY("array");

    private final String value;
}
