package com.keypoint.keypointtravel.receipt.dto.useCase.createReceiptUseCase;

import com.keypoint.keypointtravel.receipt.dto.request.createReceiptRequest.CreatePaymentItemRequest;
import com.keypoint.keypointtravel.receipt.entity.PaymentItem;
import com.keypoint.keypointtravel.receipt.entity.Receipt;
import java.util.List;
import lombok.Getter;

@Getter
public class CreatePaymentItemUseCase {

    private String itemName;
    private float amount;
    private long quantity;
    private List<Long> memberIds;

    public CreatePaymentItemUseCase(CreatePaymentItemRequest request) {
        this.itemName = request.getItemName();
        this.amount = request.getAmount();
        this.quantity = request.getQuantity();
        this.memberIds = request.getMemberIds();
    }

    public PaymentItem toEntity(Receipt receipt) {
        return PaymentItem.builder()
            .receipt(receipt)
            .itemName(this.itemName)
            .amount(this.amount)
            .quantity(this.quantity)
            .build();
    }
}
