package com.keypoint.keypointtravel.global.enumType.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GenderType {
    NONE(1, "설정 하지 않음"),
    MAN(2, "남성"),
    WOMAN(3, "여성");

    private final int code;
    private final String value;
}
