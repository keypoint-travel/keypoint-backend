package com.keypoint.keypointtravel.campaign.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FIndCampaignListUseCase {

    private Long memberId;
    private int size;
    private int page;
}
