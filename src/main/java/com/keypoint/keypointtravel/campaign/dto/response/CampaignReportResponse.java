package com.keypoint.keypointtravel.campaign.dto.response;

import com.keypoint.keypointtravel.campaign.dto.response.details.CampaignDetailsResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CampaignReportResponse {

    private CampaignDetailsResponse campaignDetails;
    CampaignReportPrice prices;
}
