package com.keypoint.keypointtravel.global.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationErrorCode implements ErrorCode {
    NOT_EXISTED_REQUEST_PUSH("001_NOT_EXISTED_REQUEST_PUSH", "존재하지 않는 푸시 요청입니다.");

    private final String code;
    private final String msg;
}
