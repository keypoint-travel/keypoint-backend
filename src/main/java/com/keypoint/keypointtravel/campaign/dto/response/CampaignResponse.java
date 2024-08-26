package com.keypoint.keypointtravel.campaign.dto.response;

import com.keypoint.keypointtravel.campaign.dto.dto.CampaignInfoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class CampaignResponse {

    private Long campaignId;
    private String campaignImage;
    private String title;
    private String startDate;
    private String endDate;
    private List<PercentageByCategory> percentages;

    public static CampaignResponse of(CampaignInfoDto campaign, List<PercentageByCategory> percentages) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy.MM.dd");
        return CampaignResponse.builder()
            .campaignId(campaign.getCampaignId())
            .campaignImage(campaign.getCampaignImage())
            .title(campaign.getTitle())
            .startDate(campaign.getStartDate().toLocalDate().format(formatter))
            .endDate(campaign.getEndDate().toLocalDate().format(formatter))
            .percentages(percentages)
            .build();
    }
}
