package com.keypoint.keypointtravel.global.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReceiptError implements ErrorCode {
    RECEIPT_RECOGNITION_FAILED("001_RECEIPT_RECOGNITION_FAILED", "영수증 인식에 실패하였습니다."),
    NOT_EXISTED_RECEIPT("002_NOT_EXISTED_RECEIPT", "찾을 수 없는 영수증 입니다."),
    NOT_EXISTED_PAYMENTITEM_IN_RECEIPT("003_NOT_EXISTED_PAYMENTITEM_IN_RECEIPT", "영수증 항목에 존재하지 않는 결제 항목이 요청되었습니다.");

    private final String code;
    private final String msg;
}
