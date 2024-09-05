package com.keypoint.keypointtravel.global.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PremiumErrorCode implements ErrorCode {

    INCORRECT_RECEIPT("001_INCORRECT_RECEIPT", "결제 정보가 올바르지 않습니다.");
    private final String code;
    private final String msg;
}
