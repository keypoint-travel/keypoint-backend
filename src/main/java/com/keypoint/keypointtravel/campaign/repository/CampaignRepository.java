package com.keypoint.keypointtravel.campaign.repository;

import com.keypoint.keypointtravel.campaign.entity.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampaignRepository extends JpaRepository<Campaign, Long>,
    CustomCampaignRepository {

}
