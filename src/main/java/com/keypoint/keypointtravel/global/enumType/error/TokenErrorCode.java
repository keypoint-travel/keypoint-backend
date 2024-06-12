package com.keypoint.keypointtravel.global.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TokenErrorCode implements ErrorCode {

    NONE("000_NONE", null),
    EXPIRED_TOKEN("001_EXPIRED_TOKEN", "만료된 토크입니다."),
    UNSUPPORTED_TOKEN("002_UNSUPPORTED_TOKEN", "지원하지 않는 토큰입니다."),
    INVALID_TOKEN("003_INVALID_TOKEN", "유효하지 않는 토큰입니다.");

    private final String code;
    private final String msg;
}
