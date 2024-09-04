package com.keypoint.keypointtravel.receipt.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReceiptIdUseCase {

    Long receiptId;

    public static ReceiptIdUseCase from(Long receiptId) {
        return new ReceiptIdUseCase(receiptId);
    }
}
