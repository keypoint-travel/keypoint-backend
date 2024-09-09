package com.keypoint.keypointtravel.campaign.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CampaignEmailUseCase {

    private String email;
    private Long memberId;
    private Long campaignId;
}
