package com.keypoint.keypointtravel.global.enumType.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GenderType {
    NONE(1, "설정 하지 않음"),
    MALE(2, "남성"),
    FEMALE(3, "여성");

    private final int code;
    private final String value;
}
