package com.keypoint.keypointtravel.receipt.dto.useCase.createReceiptUseCase;

import java.time.LocalDateTime;
import java.util.List;

import com.keypoint.keypointtravel.global.enumType.receipt.ReceiptCategory;
import com.keypoint.keypointtravel.receipt.dto.request.createReceiptRequest.CreatePaymentItemRequest;

import lombok.Getter;

@Getter
public class CreateReceiptUseCase {
    private Long campaignId;
    private String store;
    private String storeAddress;
    private ReceiptCategory receiptCategory;
    private LocalDateTime paidAt;
    private long totalAccount;
    private List<CreatePaymentItemRequest> paymentItems;
    private Double longitude;
    private Double latitude;
}
