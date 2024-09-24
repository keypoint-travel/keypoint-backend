package com.keypoint.keypointtravel.campaign.repository;

import com.keypoint.keypointtravel.campaign.entity.InvitationProhibitionHistory;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface InvitationProhibitionHistoryRepository extends
    CrudRepository<InvitationProhibitionHistory, String> {

    boolean existsByCampaignId(Long campaignId);

    List<InvitationProhibitionHistory> findAllByCampaignId(Long campaignId);
}
