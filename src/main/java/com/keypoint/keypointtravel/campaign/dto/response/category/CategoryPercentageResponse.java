package com.keypoint.keypointtravel.campaign.dto.response.category;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CategoryPercentageResponse {

    private String currency;
    private float usedTotalAmount;
    private float remainingBudget;
    private List<PercentageByCategory> percentages;
}
