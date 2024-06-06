package com.keypoint.keypointtravel.common.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {
    UNKNOWN("000_UNKNOWN", "알 수 없는 에러가 발생했습니다."),
    INVALID_REQUEST_DATA("001_INVALID_REQUEST_DATA", "유효하지 않은 데이터 입니다"),
    CRYPTOGRAPHY_FAILED("002_CRYPTOGRAPHY_FAILED", "암호화/복호화에 실패하셨습니다."),
    OPEN_API_REQUEST_FAIL("003_OPEN_API_REQUEST_FAIL", "외부 API 요청에 실패하였습니다."),

    ACCESS_DENIED_ACCOUNT("301_ACCESS_DENIED_ACCOUNT", "접근 권한이 없는 요청입니다.");
    private final String code;
    private final String msg;
}
