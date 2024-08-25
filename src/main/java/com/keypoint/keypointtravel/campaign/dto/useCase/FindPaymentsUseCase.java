package com.keypoint.keypointtravel.campaign.dto.useCase;

import com.keypoint.keypointtravel.global.enumType.currency.CurrencyType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FindPaymentsUseCase {

    private Long campaignId;
    private Long memberId;
    private CurrencyType currencyType;
    private int size;
    private int page;

    public FindPaymentsUseCase(Long campaignId, Long memberId, String currencyType, int size, int page) {
        this.campaignId = campaignId;
        this.memberId = memberId;
        this.size = size;
        this.page = page;
        switch (currencyType) {
            case "KRW" -> this.currencyType = CurrencyType.KRW;
            case "USD" -> this.currencyType = CurrencyType.USD;
            case "JPY" -> this.currencyType = CurrencyType.JPY;
            default -> this.currencyType = null;
        }
    }
}
