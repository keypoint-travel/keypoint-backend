package com.keypoint.keypointtravel.global.enumType.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CountryCode {
    NONE("", "알수없음");

    private final String code;
    private final String description;
}
