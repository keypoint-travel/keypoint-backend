package com.keypoint.keypointtravel.campaign.repository;

import com.keypoint.keypointtravel.campaign.entity.MemberCampaign;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberCampaignRepository extends JpaRepository<MemberCampaign, Long> {

    List<MemberCampaign> findAllByCampaignId(Long campaignId);

    boolean existsByCampaignIdAndMemberId(Long campaignId, Long memberId);
}
