package com.keypoint.keypointtravel.premium.dto.useCase;

import com.keypoint.keypointtravel.global.enumType.currency.CurrencyType;
import com.keypoint.keypointtravel.premium.dto.apple.AppleAppStoreResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApplePurchaseUseCase {

    private Long memberId;
    private float amount;
    private CurrencyType currency;
    private AppleAppStoreResponse appleAppStoreResponse;
}
