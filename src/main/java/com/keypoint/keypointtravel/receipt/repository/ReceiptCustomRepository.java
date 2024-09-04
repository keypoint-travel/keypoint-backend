package com.keypoint.keypointtravel.receipt.repository;

import com.keypoint.keypointtravel.receipt.dto.response.receiptResponse.ReceiptResponse;
import com.keypoint.keypointtravel.receipt.dto.useCase.updateReceiptUseCase.UpdateReceiptUseCase;


public interface ReceiptCustomRepository {

    ReceiptResponse findReceiptDetailByReceiptId(Long receiptId);

    void deleteReceiptById(Long receiptId);

    void updateReceipt(UpdateReceiptUseCase useCase);
}
