package com.keypoint.keypointtravel.campaign.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JoinByEmailUseCase {
    Long memberId;
    String email;
    Long campaignId;
}