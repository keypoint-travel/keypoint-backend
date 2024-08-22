package com.keypoint.keypointtravel.campaign.repository;

import com.keypoint.keypointtravel.campaign.entity.Campaign;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CampaignRepository extends JpaRepository<Campaign, Long>,
    CustomCampaignRepository {

    @Query("SELECT c.title FROM Campaign c WHERE c.id = :campaignId")
    Optional<String> findTitleByCampaignId(@Param("campaignId") Long campaignId);
}
