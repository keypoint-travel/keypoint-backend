package com.keypoint.keypointtravel.common.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {
    NOT_ALLOW_EMAIL("001_NOT_ALLOW_EMAIL", "이메일 사용이 허용이 되지 않은 사용자입니다."),
    REGISTERED_EMAIL_FOR_THE_OTHER("002_REGISTERED_EMAIL_FOR_THE_OTHER",
        "다른 소셜 서비스로 이미 등록된 이메일입니다."),
    NOT_EXISTED_EMAIL("003_NOT_EXISTED_EMAIL", "존재하지 않은 이메일입니다."),
    DUPLICATED_EMAIL("004_DUPLICATED_EMAIL", "이미 등록되어 있는 이메일입니다."),
    INVALID_PASSWORD("005_INVALID_PASSWORD", "유효하지 않은 비밀번호입니다.");

    private final String code;
    private final String msg;
}
