package com.keypoint.keypointtravel.campaign.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class DetailsByPriceResponse {

    private String currency;
    private float totalAmount;
    private float remainingBudget;
    private List<PricePaymentInfo> payments;
}
