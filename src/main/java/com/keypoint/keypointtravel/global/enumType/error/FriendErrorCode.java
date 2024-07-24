package com.keypoint.keypointtravel.global.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FriendErrorCode implements ErrorCode {

    CANNOT_ADD_SELF("001_CANNOT_ADD_SELF", "자기 자신을 추가할 수 없습니다."),
    DUPLICATED_FRIEND("002_DUPLICATED_FRIEND", "이미 등록되어 있는 친구입니다."),
    NOT_EXISTED_FRIEND("003_NOT_EXISTED_FRIEND", "존재하지 않는 친구입니다.");

    private final String code;
    private final String msg;
}
