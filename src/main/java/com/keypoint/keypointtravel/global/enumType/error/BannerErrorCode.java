package com.keypoint.keypointtravel.global.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BannerErrorCode implements ErrorCode {

    REQUEST_DATA_MISMATCH("001_REQUEST_DATA_MISMATCH", "해당하는 타입이 존재하지 않습니다"),
    NOT_EXISTED_BANNER("002_NOT_EXISTED_BANNER", "존재하지 않는 배너입니다.");

    private final String code;
    private final String msg;
}
