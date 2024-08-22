package com.keypoint.keypointtravel.campaign.dto.dto;

import com.keypoint.keypointtravel.global.enumType.currency.CurrencyType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TotalBudgetDto {

    private Long totalAmount;
    private CurrencyType currencyType;
}
