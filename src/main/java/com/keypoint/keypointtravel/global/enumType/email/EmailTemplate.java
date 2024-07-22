package com.keypoint.keypointtravel.global.enumType.email;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EmailTemplate {
    EMAIL_VERIFICATION("01", "email-verification", "이메일 인증");


    private final String code;
    private final String template;
    private final String subject;
}
