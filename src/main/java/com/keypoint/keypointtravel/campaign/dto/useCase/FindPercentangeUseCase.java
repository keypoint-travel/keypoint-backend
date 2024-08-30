package com.keypoint.keypointtravel.campaign.dto.useCase;

import com.keypoint.keypointtravel.global.enumType.currency.CurrencyType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FindPercentangeUseCase {

    private Long campaignId;
    private Long memberId;
    private CurrencyType currencyType;

    public FindPercentangeUseCase(Long campaignId, Long memberId, String currencyType) {
        this.campaignId = campaignId;
        this.memberId = memberId;
        switch (currencyType) {
            case "KRW" -> this.currencyType = CurrencyType.KRW;
            case "USD" -> this.currencyType = CurrencyType.USD;
            case "JPY" -> this.currencyType = CurrencyType.JPY;
            default -> this.currencyType = null;
        }
    }
}
