package com.keypoint.keypointtravel.campaign.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
@AllArgsConstructor
public class PercentageResponse {

    private String currency;
    private float usedTotalAmount;
    private float remainingBudget;
    private List<PercentageByCategory> percentages;

    public void sortPercentages() {
        Collections.sort(percentages);
    }
}
