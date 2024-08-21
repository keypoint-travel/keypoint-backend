package com.keypoint.keypointtravel.campaign.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApproveByCodeUseCase {

    private Long leaderId;
    private boolean isApprove;
    private Long memberId;
    private Long campaignId;
}
