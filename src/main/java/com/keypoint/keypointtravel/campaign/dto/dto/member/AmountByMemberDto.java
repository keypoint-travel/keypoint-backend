package com.keypoint.keypointtravel.campaign.dto.dto.member;

import com.keypoint.keypointtravel.campaign.dto.dto.AmountDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AmountByMemberDto implements AmountDto {

    private Long memberId;
    private String memberName;
    private String memberImage;
    private double amount;

    public void updateBudget(float amount) {
        this.amount = amount;
    }

    public float getAmount() {
        return (float) amount;
    }
}
