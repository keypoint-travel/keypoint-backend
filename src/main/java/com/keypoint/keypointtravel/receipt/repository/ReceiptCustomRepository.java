package com.keypoint.keypointtravel.receipt.repository;

import com.keypoint.keypointtravel.receipt.dto.response.receiptResponse.ReceiptResponse;


public interface ReceiptCustomRepository {

    ReceiptResponse findReceiptDetailByReceiptId(Long receiptId);

    void deleteReceiptById(Long receiptId);
}
