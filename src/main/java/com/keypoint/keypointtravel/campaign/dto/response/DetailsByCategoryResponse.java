package com.keypoint.keypointtravel.campaign.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;

@Getter
@AllArgsConstructor
public class DetailsByCategoryResponse {

    private String currency;
    private float totalAmount;
    private float remainingBudget;
    private HashMap<String, Float> categoryPercentage;
    private List<PaymentInfo> payments;
}
