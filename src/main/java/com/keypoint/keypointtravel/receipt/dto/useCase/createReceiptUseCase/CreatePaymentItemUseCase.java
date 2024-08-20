package com.keypoint.keypointtravel.receipt.dto.useCase.createReceiptUseCase;

import com.keypoint.keypointtravel.receipt.dto.request.createReceiptRequest.CreatePaymentItemRequest;
import java.util.List;
import lombok.Getter;

@Getter
public class CreatePaymentItemUseCase {

    private String itemName;
    private long amount;
    private long quantity;
    private List<String> memberIds;

    public CreatePaymentItemUseCase(CreatePaymentItemRequest request) {
        this.itemName = request.getItemName();
        this.amount = request.getAmount();
        this.quantity = request.getQuantity();
        this.memberIds = request.getMemberIds();
    }
}
