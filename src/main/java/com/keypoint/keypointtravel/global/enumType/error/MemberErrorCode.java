package com.keypoint.keypointtravel.global.enumType.error;

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
    INVALID_PASSWORD("005_INVALID_PASSWORD", "유효하지 않은 비밀번호입니다."),
    INVALID_LOGIN_CREDENTIALS("006_INVALID_LOGIN_CREDENTIALS", "로그인 정보가 잘못되었습니다."),
    NOT_EXISTED_MEMBER("007_NOT_EXISTED_MEMBER", "존재하지 않는 사용자입니다."),
    NOT_GENERAL_MEMBER("008_NOT_GENERAL_MEMBER", "일반 회원이 아닌 소셜 로그인으로 등록된 회원입니다."),
    NOT_EXISTED_EMAIL_OR_INVITATION_CODE("009_NOT_EXISTED_EMAIL_OR_INVITATION_CODE",
        "이메일 혹은 초대코드가 잘못되었습니다."),
    FAIL_TO_CONFIRM_EMAIL("010_FAIL_TO_CONFIRM_EMAIL", "이메일 인증에 실패하였습니다.");

    private final String code;
    private final String msg;
}
