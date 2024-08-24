package com.keypoint.keypointtravel.campaign.dto.dto.date;

import com.keypoint.keypointtravel.campaign.dto.dto.AmountDto;
import com.keypoint.keypointtravel.global.enumType.receipt.ReceiptCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AmountByDateDto implements AmountDto {

    private String date;
    private float amount;

    public void updateBudget(float amount) {
        this.amount = amount;
    }
}
