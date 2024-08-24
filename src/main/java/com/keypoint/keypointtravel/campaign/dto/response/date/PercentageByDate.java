package com.keypoint.keypointtravel.campaign.dto.response.date;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PercentageByDate {

    private String date;
    private float price;
    private float percentage;

    public void updatePercentage(float percentage) {
        this.percentage = percentage;
    }
}
