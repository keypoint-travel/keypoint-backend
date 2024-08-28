package com.keypoint.keypointtravel.campaign.dto.response;

import com.keypoint.keypointtravel.campaign.dto.dto.CampaignInfoDto;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Builder
public class CampaignResponse {

    private Long campaignId;
    private String campaignImage;
    private String title;
    private String startDate;
    private String endDate;

    public static List<CampaignResponse> from(List<CampaignInfoDto> campaigns) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy.MM.dd");
        return campaigns.stream()
            .map(campaign -> CampaignResponse.builder()
                .campaignId(campaign.getCampaignId())
                .campaignImage(campaign.getCampaignImage())
                .title(campaign.getTitle())
                .startDate(campaign.getStartDate().toLocalDate().format(formatter))
                .endDate(campaign.getEndDate().toLocalDate().format(formatter))
                .build())
            .toList();
    }
}
