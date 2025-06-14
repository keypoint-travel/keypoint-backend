package com.keypoint.keypointtravel.campaign.repository;

import com.keypoint.keypointtravel.campaign.entity.CampaignBudget;
import com.keypoint.keypointtravel.global.enumType.currency.CurrencyType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CampaignBudgetRepository extends JpaRepository<CampaignBudget, Long> {

    @Query("SELECT cb.currency FROM CampaignBudget cb WHERE cb.campaign.id = :campaignId")
    Optional<CurrencyType> findCurrencyByCampaignId(@Param("campaignId") Long campaignId);

    List<CampaignBudget> findAllByCampaignId(Long campaignId);

    void deleteAllByCampaignId(Long campaignId);
}
