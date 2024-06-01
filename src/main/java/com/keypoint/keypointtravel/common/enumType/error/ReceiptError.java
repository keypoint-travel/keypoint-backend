package com.keypoint.keypointtravel.common.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReceiptError implements ErrorCode {
    RECEIPT_RECOGNITION_FAILED("001_RECEIPT_RECOGNITION_FAILED", "영수증 인식에 실패하였습니다.");

    private final String code;
    private final String msg;
}
