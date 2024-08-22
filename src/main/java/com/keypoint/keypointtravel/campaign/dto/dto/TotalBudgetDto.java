package com.keypoint.keypointtravel.campaign.dto.dto;

import com.keypoint.keypointtravel.global.enumType.currency.CurrencyType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TotalBudgetDto {

    private float totalAmount;
    private CurrencyType currencyType;

    public void updateTotalBudget(float amount, CurrencyType currencyType) {
        this.totalAmount = amount;
        this.currencyType = currencyType;
    }
}
