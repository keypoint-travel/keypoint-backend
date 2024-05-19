package com.keypoint.keypointtravel.common.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {
    // 400
    UNKNOWN("000_UNKNOWN", "알 수 없는 에러가 발생했습니다."),
    INVALID_REQUEST_DATA("001_INVALID_REQUEST_DATA", "유효하지 않은 데이터 입니다");

    private final String code;
    private final String msg;
}
