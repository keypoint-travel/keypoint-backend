package com.keypoint.keypointtravel.global.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GuideErrorCode implements ErrorCode {
    DUPLICATED_ORDER("001_DUPLICATED_ORDER",
        "이미 존재하는 순서 번호 입니다. 해당 순서 번호를 가진 이용 가이드를 수정하거나 다른 순서 번호로 등록해주세요.");

    private final String code;
    private final String msg;
}
