package com.keypoint.keypointtravel.receipt.dto.useCase.updateReceiptUseCase;

import com.keypoint.keypointtravel.receipt.dto.request.updateReceiptRequest.UpdatePaymentItemRequest;
import com.keypoint.keypointtravel.receipt.entity.PaymentItem;
import com.keypoint.keypointtravel.receipt.entity.Receipt;
import lombok.Getter;

import java.util.List;

@Getter
public class UpdatePaymentItemUseCase {

    private Long paymentItemId;
    private String itemName;
    private float amount;
    private long quantity;
    private List<Long> memberIds;

    public UpdatePaymentItemUseCase(UpdatePaymentItemRequest request) {
        this.paymentItemId = request.getPaymentItemId();
        this.itemName = request.getItemName();
        this.amount = request.getAmount();
        this.quantity = request.getQuantity();
        this.memberIds = request.getMemberIds();
    }

    public PaymentItem toEntity(Receipt receipt) {
        return new PaymentItem(
                receipt,
                itemName,
                quantity,
                amount
        );
    }
}
