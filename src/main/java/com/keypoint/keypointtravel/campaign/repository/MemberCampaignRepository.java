package com.keypoint.keypointtravel.campaign.repository;

import com.keypoint.keypointtravel.campaign.entity.MemberCampaign;
import com.keypoint.keypointtravel.member.entity.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberCampaignRepository extends JpaRepository<MemberCampaign, Long> {

    List<MemberCampaign> findAllByCampaignId(Long campaignId);

    boolean existsByCampaignIdAndMemberId(Long campaignId, Long memberId);

    @Query("SELECT mc.member "
        + "FROM MemberCampaign mc "
        + "WHERE mc.campaign.id = :campaignId")
    List<Member> findMembersByCampaignId(@Param("campaignId") Long campaignId);

    void deleteAllByCampaignId(Long campaignId);
}
