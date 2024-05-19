package com.keypoint.keypointtravel.common.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {
    // 400
    UNKNOWN("000_UNKNOWN", "알 수 없는 에러가 발생했습니다.");

    private final String code;
    private final String msg;
}
