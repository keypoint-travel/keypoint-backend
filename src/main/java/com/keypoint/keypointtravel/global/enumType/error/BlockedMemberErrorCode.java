package com.keypoint.keypointtravel.global.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BlockedMemberErrorCode implements ErrorCode {

    ALREADY_BLOCKED("001_ALREADY_BLOCKED", "이미 차단한 회원입니다."),
    NOT_EXISTED_BLOCKED("002_NOT_EXISTED_BLOCKED", "차단한 기록이 없습니다."),
    CANNOT_BLOCK_SELF("003_CANNOT_BLOCK_SELF", "자기 자신을 차단할 수 없습니다."),
    EXISTS_BLOCKED_MEMBER("004_EXISTS_BLOCKED_MEMBER", "참여자간 차단된 유저가 있습니다. 확인 후 다시 시도해주세요.");

    private final String code;
    private final String msg;
}
