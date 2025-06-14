package com.keypoint.keypointtravel.global.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {
    UNKNOWN("000_UNKNOWN", "알 수 없는 에러가 발생했습니다."),
    INVALID_REQUEST_DATA("001_INVALID_REQUEST_DATA", "유효하지 않은 데이터 입니다"),
    CRYPTOGRAPHY_FAILED("002_CRYPTOGRAPHY_FAILED", "암호화/복호화에 실패하셨습니다."),
    OPEN_API_REQUEST_FAIL("003_OPEN_API_REQUEST_FAIL", "외부 API 요청에 실패하였습니다."),
    FAIL_TO_CONVERT_FILE("004_FAIL_TO_CONVERT_FILE", "파일을 변환하는데 실패하였습니다."),
    FAIL_TO_SEND_EMAIL("005_FAIL_TO_SEND_EMAIL", "이메일 전송에 실패하였습니다."),
    FAIL_TO_FIND_LANGUAGE("006_FAIL_TO_FIND_LANGUAGE", "다국어를 찾는데 실패했습니다."),
    FAILED_TO_CONVERT_IMAGE("007_FAILED_TO_CONVERT_IMAGE", "이미지로 변환하는 것에 실패하였습니다."),
    DUPLICATED_ORDER("008_DUPLICATED_ORDER",
        "이미 존재하는 순서 번호 입니다. 해당 순서 번호를 가진 데이터를 수정하거나 다른 순서 번호로 등록해주세요."),
    FAIL_TO_DELETE_EN_DATA("009_FAIL_TO_DELETE_EN_DATA", "영어 버전 데이터를 변경(삭제)하는 것은 불가능합니다."),

    ACCESS_DENIED_ACCOUNT("301_ACCESS_DENIED_ACCOUNT", "접근 권한이 없는 요청입니다.");
    private final String code;
    private final String msg;
}
