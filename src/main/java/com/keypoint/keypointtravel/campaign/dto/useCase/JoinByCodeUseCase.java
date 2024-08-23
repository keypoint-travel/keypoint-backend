package com.keypoint.keypointtravel.campaign.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JoinByCodeUseCase {

    private final Long memberId;
    private final String campaignCode;
}
