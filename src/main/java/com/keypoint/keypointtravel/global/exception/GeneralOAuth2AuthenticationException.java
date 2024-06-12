package com.keypoint.keypointtravel.global.exception;

import com.keypoint.keypointtravel.global.enumType.error.CommonErrorCode;
import com.keypoint.keypointtravel.global.enumType.error.ErrorCode;
import lombok.Getter;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

@Getter
public class GeneralOAuth2AuthenticationException extends OAuth2AuthenticationException {

    private final ErrorCode errorCode;

    public GeneralOAuth2AuthenticationException() {
        super(CommonErrorCode.UNKNOWN.getMsg());
        this.errorCode = CommonErrorCode.UNKNOWN;
    }

    public GeneralOAuth2AuthenticationException(ErrorCode code) {
        super(code.getMsg());
        this.errorCode = code;
    }
}
