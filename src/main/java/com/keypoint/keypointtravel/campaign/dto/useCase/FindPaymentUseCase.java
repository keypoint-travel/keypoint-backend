package com.keypoint.keypointtravel.campaign.dto.useCase;

import com.keypoint.keypointtravel.global.enumType.currency.CurrencyType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FindPaymentUseCase {

    private Long campaignId;
    private Long memberId;
    private CurrencyType currencyType;
}
