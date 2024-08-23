package com.keypoint.keypointtravel.campaign.dto.dto;

import com.keypoint.keypointtravel.global.enumType.currency.CurrencyType;
import com.keypoint.keypointtravel.global.enumType.receipt.ReceiptCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PaymentDto {

    private Long receiptId;
    private ReceiptCategory category;

    private LocalDateTime paidAt;

    private Long paymentItemId;

    private String store;

    private Long quantity;

    private float amount;

    private CurrencyType currencyType;

    public void updateBudget(float amount, CurrencyType currencyType) {
        this.amount = amount;
        this.currencyType = currencyType;
    }

    public float getPrice() {
        return amount * quantity;
    }
}
