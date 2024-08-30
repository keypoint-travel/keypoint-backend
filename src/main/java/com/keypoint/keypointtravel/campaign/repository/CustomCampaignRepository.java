package com.keypoint.keypointtravel.campaign.repository;

import com.keypoint.keypointtravel.campaign.dto.dto.CampaignInfoDto;
import com.keypoint.keypointtravel.campaign.dto.dto.SendInvitationEmailDto;
import com.keypoint.keypointtravel.campaign.dto.dto.TravelLocationDto;

import java.util.List;

public interface CustomCampaignRepository {

    SendInvitationEmailDto findSendInvitationEmailInfo(Long campaignId);

    CampaignInfoDto findCampaignInfo(Long campaignId);

    List<CampaignInfoDto> findCampaignInfoList(Long memberId);

    void updateCampaignFinished(Long campaignId);

    List<TravelLocationDto> findTravelLocationList(Long campaignId);
}
