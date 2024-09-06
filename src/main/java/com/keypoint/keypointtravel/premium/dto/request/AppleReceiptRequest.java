package com.keypoint.keypointtravel.premium.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.keypoint.keypointtravel.global.enumType.currency.CurrencyType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AppleReceiptRequest {

    @JsonAlias("receipt-data")
    private String receiptData;
    @NotNull
    private float amount;
    @NotNull
    private CurrencyType currency;
}
