package com.keypoint.keypointtravel.campaign.dto.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Date;

@Getter
@AllArgsConstructor
public class CampaignInfoDto {

    private Long campaignId;
    private String campaignImage;
    private String title;
    private Date startDate;
    private Date endDate;
}
