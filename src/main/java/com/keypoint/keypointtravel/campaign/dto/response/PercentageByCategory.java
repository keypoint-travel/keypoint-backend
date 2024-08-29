package com.keypoint.keypointtravel.campaign.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PercentageByCategory implements Comparable<PercentageByCategory> {

    private String category;
    private float price;
    private float percentage;

    public void updatePercentage(float percentage) {
        this.percentage = percentage;
    }

    @Override
    public int compareTo(PercentageByCategory category) {
        return category.getCategory().compareTo(this.category);
    }
}
