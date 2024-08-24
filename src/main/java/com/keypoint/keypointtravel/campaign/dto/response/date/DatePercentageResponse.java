package com.keypoint.keypointtravel.campaign.dto.response.date;

import com.keypoint.keypointtravel.campaign.dto.response.category.PercentageByCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class DatePercentageResponse {

    private String currency;
    private float usedTotalAmount;
    private float remainingBudget;
    private List<PercentageByCategory> percentages;
}
