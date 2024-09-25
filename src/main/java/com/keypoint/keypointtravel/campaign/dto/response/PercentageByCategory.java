package com.keypoint.keypointtravel.campaign.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public class PercentageByCategory implements Comparable<PercentageByCategory> {

    private static final List<String> order = Arrays.asList("food_expenses", "transportation_expenses", "accommodation_expenses", "shopping_expenses", "other_expenses", "remaining_budget");

    private String category;
    private float amount;
    private float percentage;

    public void updatePercentage(float percentage) {
        this.percentage = percentage;
    }

    @Override
    public int compareTo(PercentageByCategory other) {
        int index1 = order.indexOf(this.category);
        int index2 = order.indexOf(other.getCategory());
        return Integer.compare(index1, index2);
    }
}
