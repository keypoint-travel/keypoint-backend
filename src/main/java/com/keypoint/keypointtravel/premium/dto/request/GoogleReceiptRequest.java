package com.keypoint.keypointtravel.premium.dto.request;

import com.keypoint.keypointtravel.global.enumType.currency.CurrencyType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GoogleReceiptRequest {

    @NotNull
    private String packageName;
    @NotNull
    private String productId;
    @NotNull
    private String purchaseToken;
    @NotNull
    private float amount;
    @NotNull
    private CurrencyType currency;
}
