package com.keypoint.keypointtravel.premium.dto.useCase;

import com.keypoint.keypointtravel.global.enumType.currency.CurrencyType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GooglePurchaseUseCase {

    private Long memberId;
    private String packageName;
    private String productId;
    private String purchaseToken;
    private float amount;
    private CurrencyType currency;
}
