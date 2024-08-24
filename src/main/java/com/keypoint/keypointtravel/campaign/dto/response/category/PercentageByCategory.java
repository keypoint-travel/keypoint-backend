package com.keypoint.keypointtravel.campaign.dto.response.category;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PercentageByCategory {

    private String category;
    private float price;
    private float percentage;

    public void updatePercentage(float percentage) {
        this.percentage = percentage;
    }
}
