package com.keypoint.keypointtravel.campaign.dto.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SendInvitationEmailDto {

    private String leaderName;
    private String campaignName;
    private String campaignCode;
}
