package com.keypoint.keypointtravel.campaign.repository;

public interface CustomCampaignRepository {

    boolean existsByCampaignLeaderTrue(Long memberId, Long campaignId);

    boolean existsBlockedMemberInCampaign(Long memberId, Long campaignId);


}
