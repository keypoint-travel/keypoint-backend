package com.keypoint.keypointtravel.campaign.dto.dto.member;

import com.keypoint.keypointtravel.campaign.dto.dto.AmountDto;
import com.keypoint.keypointtravel.global.enumType.receipt.ReceiptCategory;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MemberAmountByCategoryDto implements AmountDto {

    private ReceiptCategory category;
    private double amount;

    public void updateBudget(float amount) {
        this.amount = amount;
    }

    public float getAmount() {
        return (float) amount;
    }

    public ReceiptCategory getCategory() {
        return category;
    }
}
