package com.keypoint.keypointtravel.campaign.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApproveByCodeRequest {

    private boolean isApprove;
    private Long memberId;
    private Long campaignId;
}
