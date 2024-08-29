package com.keypoint.keypointtravel.campaign.repository;

import com.keypoint.keypointtravel.campaign.dto.dto.CampaignInfoDto;
import com.keypoint.keypointtravel.campaign.dto.dto.SendInvitationEmailDto;
import com.keypoint.keypointtravel.campaign.entity.MemberCampaign;

import java.util.List;

public interface CustomCampaignRepository {

    boolean existsByCampaignLeaderTrue(Long memberId, Long campaignId);

    boolean existsBlockedMemberInCampaign(Long memberId, Long campaignId);

    SendInvitationEmailDto findSendInvitationEmailInfo(Long campaignId);

    MemberCampaign findCampaignLeader(Long campaignId);

    List<MemberCampaign> findMembersByCampaignCode(String campaignCode);

    List<MemberCampaign> findMembersByCampaignCode(Long campaignId);

    CampaignInfoDto findCampaignInfo(Long campaignId);
}
