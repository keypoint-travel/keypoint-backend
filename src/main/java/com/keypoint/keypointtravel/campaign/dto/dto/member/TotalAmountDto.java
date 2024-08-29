package com.keypoint.keypointtravel.campaign.dto.dto.member;

import com.keypoint.keypointtravel.global.enumType.currency.CurrencyType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TotalAmountDto {

    private float totalAmount;
    private float usedAmount;
    private Long totalMember;
    private CurrencyType currency;
}
