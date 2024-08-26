package com.keypoint.keypointtravel.campaign.dto.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AmountByMemberDto {

    private Long memberId;
    private String memberName;
    private String memberImage;
    private double amount;

    public void updateBudget(float amount) {
        this.amount = amount;
    }
}
