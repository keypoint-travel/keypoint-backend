package com.keypoint.keypointtravel.receipt.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CampaignIdUseCase {

    Long campaignId;

    public static CampaignIdUseCase from(Long campaignId) {
        return new CampaignIdUseCase(campaignId);
    }
}
