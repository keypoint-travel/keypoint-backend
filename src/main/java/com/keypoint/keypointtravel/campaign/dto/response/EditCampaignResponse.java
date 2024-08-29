package com.keypoint.keypointtravel.campaign.dto.response;

import com.keypoint.keypointtravel.campaign.dto.dto.CampaignInfoDto;
import com.keypoint.keypointtravel.campaign.dto.dto.MemberInfoDto;
import com.keypoint.keypointtravel.campaign.dto.dto.TravelLocationDto;
import com.keypoint.keypointtravel.campaign.entity.CampaignBudget;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@AllArgsConstructor
public class EditCampaignResponse {

    private final Long campaignId;
    private final String title;
    private final String image;
    private final String startDate;
    private final String endDate;
    private final List<TravelLocationDto> travels;
    private final List<CampaignBudgetInfo> budgets;
    private final List<MemberInfoDto> members;

    public EditCampaignResponse(CampaignInfoDto campaignInfoDto,
                                List<TravelLocationDto> travels, List<CampaignBudget> budgets,
                                List<MemberInfoDto> members) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.campaignId = campaignInfoDto.getCampaignId();
        this.title = campaignInfoDto.getTitle();
        this.image = campaignInfoDto.getCampaignImage();
        this.startDate = campaignInfoDto.getStartDate().toLocalDate().format(formatter);
        this.endDate = campaignInfoDto.getEndDate().toLocalDate().format(formatter);
        this.travels = travels;
        this.budgets = budgets.stream().map(CampaignBudgetInfo::from).toList();
        this.members = members;
    }
}
