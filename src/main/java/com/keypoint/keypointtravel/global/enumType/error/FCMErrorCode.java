package com.keypoint.keypointtravel.global.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FCMErrorCode implements ErrorCode {
    NOT_REGISTERED_FCM_TOKEN("001_NOT_REGISTERED_FCM_TOKEN", "등록되지 않은 FCM 토큰입니다."),
    FAIL_TO_SEND_FCM("002_FAIL_TO_SEND_FCM", "FCM을 보내는데 실패했습니다.");

    private final String code;
    private final String msg;
}
