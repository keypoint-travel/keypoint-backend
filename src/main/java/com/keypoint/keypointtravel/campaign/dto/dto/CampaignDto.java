package com.keypoint.keypointtravel.campaign.dto.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CampaignDto {

    private List<CampaignInfoDto> dtoList;
    private Long totalCount;

}
