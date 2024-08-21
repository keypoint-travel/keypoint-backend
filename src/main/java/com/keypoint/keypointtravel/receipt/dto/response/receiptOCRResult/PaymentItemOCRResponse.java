package com.keypoint.keypointtravel.receipt.dto.response.receiptOCRResult;

import com.keypoint.keypointtravel.api.dto.response.ReceiptItemUseCase;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentItemOCRResponse {

    private String itemName;
    private Float amount;
    private Long quantity;

    public PaymentItemOCRResponse(ReceiptItemUseCase useCase) {
        this.itemName = useCase.getDescription();
        this.amount = useCase.getTotalPrice();
        this.quantity = useCase.getQuantity() == null ? null : useCase.getQuantity().longValue();

    }
}
