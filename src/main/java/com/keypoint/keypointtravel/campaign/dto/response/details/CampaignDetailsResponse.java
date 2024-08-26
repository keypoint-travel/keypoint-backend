package com.keypoint.keypointtravel.campaign.dto.response.details;

import com.keypoint.keypointtravel.campaign.dto.dto.CampaignInfoDto;
import com.keypoint.keypointtravel.campaign.dto.dto.MemberInfoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class CampaignDetailsResponse {

    private Long campaignId;
    private String campaignImage;
    private String title;
    private String startDate;
    private String endDate;
    private List<MemberInfoDto> members;
    private List<ReceiptInfo> receipts;

    public static CampaignDetailsResponse of(CampaignInfoDto campaign, List<MemberInfoDto> members, List<ReceiptInfo> receipts) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy.MM.dd");
        return CampaignDetailsResponse.builder()
            .campaignId(campaign.getCampaignId())
            .campaignImage(campaign.getCampaignImage())
            .title(campaign.getTitle())
            .startDate(campaign.getStartDate().toLocalDate().format(formatter))
            .endDate(campaign.getEndDate().toLocalDate().format(formatter))
            .members(members)
            .receipts(receipts)
            .build();
    }
}
