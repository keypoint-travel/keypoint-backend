package com.keypoint.keypointtravel.global.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BlockedMemberErrorCode implements ErrorCode {

    ALREADY_BLOCKED("001_ALREADY_BLOCKED", "이미 차단된 회원입니다.");

    private final String code;
    private final String msg;
}
