package com.keypoint.keypointtravel.campaign.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JoinByCampaignCodeRequest {

    @NotEmpty(message = "캠페인 코드를 입력해주세요.")
    @NotBlank(message = "캠페인 코드를 입력해주세요.")
    private String campaignCode;
}
