package com.keypoint.keypointtravel.campaign.dto.dto.category;

import com.keypoint.keypointtravel.campaign.dto.dto.AmountDto;
import com.keypoint.keypointtravel.global.enumType.receipt.ReceiptCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AmountByCategoryDto implements AmountDto {

    private ReceiptCategory category;
    private float amount;

    public void updateBudget(float amount) {
        this.amount = amount;
    }
}
