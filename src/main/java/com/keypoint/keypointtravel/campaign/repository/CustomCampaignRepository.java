package com.keypoint.keypointtravel.campaign.repository;

import com.keypoint.keypointtravel.campaign.dto.dto.SendInvitationEmailDto;

public interface CustomCampaignRepository {

    boolean existsByCampaignLeaderTrue(Long memberId, Long campaignId);

    boolean existsBlockedMemberInCampaign(Long memberId, Long campaignId);

    SendInvitationEmailDto findSendInvitationEmailInfo(Long campaignId);
}
