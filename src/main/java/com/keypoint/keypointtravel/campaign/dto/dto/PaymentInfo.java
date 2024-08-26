package com.keypoint.keypointtravel.campaign.dto.dto;

import com.keypoint.keypointtravel.global.enumType.currency.CurrencyType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PaymentInfo implements AmountDto {

    private Long paymentItemId;
    private String storeName;
    private LocalDateTime paidAt;
    private float amount;
    private Long quantity;
    private CurrencyType currencyType;
    private Long receiptId;

    @Override
    public void updateBudget(float amount) {
        this.amount = amount;
    }

    public PaymentInfo(Long paymentItemId, String storeName, LocalDateTime paidAt, double amount, Long quantity,
                       CurrencyType currencyType, Long receiptId) {
        this.paymentItemId = paymentItemId;
        this.storeName = storeName;
        this.paidAt = paidAt;
        this.amount = (float)amount;
        this.quantity = quantity;
        this.currencyType = currencyType;
        this.receiptId = receiptId;
    }
}
