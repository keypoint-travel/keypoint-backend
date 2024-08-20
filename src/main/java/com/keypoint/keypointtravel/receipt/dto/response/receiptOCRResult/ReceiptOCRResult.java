package com.keypoint.keypointtravel.receipt.dto.response.receiptOCRResult;

import java.time.LocalDateTime;
import java.util.List;

public class ReceiptOCRResult {
    private String store;
    private String storeAddress;
    private LocalDateTime paidAt;
    private long totalAccount;
    private List<PaymentItemOCRResult> paymentItems;
}
