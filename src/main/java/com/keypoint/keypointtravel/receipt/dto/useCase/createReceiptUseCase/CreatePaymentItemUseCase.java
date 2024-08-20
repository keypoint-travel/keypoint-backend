package com.keypoint.keypointtravel.receipt.dto.useCase.createReceiptUseCase;

import java.util.List;

import lombok.Getter;

@Getter
public class CreatePaymentItemUseCase {
    private String itemName;
    private long amount;
    private long quantity;
    private List<String> memberIds;
}
