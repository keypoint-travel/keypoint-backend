package com.keypoint.keypointtravel.campaign.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CompleteCampaignUseCase {

    private Long campaignId;
    private Long memberId;
}
