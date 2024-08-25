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
}
