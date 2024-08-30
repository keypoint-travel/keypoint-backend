package com.keypoint.keypointtravel.campaign.repository;

import com.keypoint.keypointtravel.campaign.entity.CampaignWaitMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampaignWaitMemberRepository extends JpaRepository<CampaignWaitMember, Long> {

    boolean existsByCampaignIdAndMemberId(Long campaignId, Long memberId);

    void deleteAllByCampaignId(Long campaignId);
}
