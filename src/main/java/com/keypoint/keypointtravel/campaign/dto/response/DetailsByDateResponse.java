package com.keypoint.keypointtravel.campaign.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class DetailsByDateResponse {

    private String currency;
    private float totalAmount;
    private float remainingBudget;
    private HashMap<String, Float> percentage;
    private List<DatePaymentInfo> payments;

    public void sortPayments() {
        Collections.sort(payments);
    }

    public static DetailsByDateResponse from(DetailsByCategoryResponse response) {
        return DetailsByDateResponse.builder()
            .currency(response.getCurrency())
            .totalAmount(response.getTotalAmount())
            .remainingBudget(response.getRemainingBudget())
            .percentage(response.getPercentage())
            .payments(response.getPayments().stream().map(DatePaymentInfo::from).collect(Collectors.toList()))
            .build();
    }
}
