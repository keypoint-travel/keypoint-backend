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

    public FindPaymentUseCase(Long campaignId, Long memberId, String currencyType) {
        this.campaignId = campaignId;
        this.memberId = memberId;
        if(currencyType.equals("KRW")){
            this.currencyType = CurrencyType.KRW;
        } else if (currencyType.equals("USD")){
            this.currencyType = CurrencyType.USD;
        } else if (currencyType.equals("JPY")){
            this.currencyType = CurrencyType.JPY;
        } else {
            this.currencyType = null;
        }
    }
}
