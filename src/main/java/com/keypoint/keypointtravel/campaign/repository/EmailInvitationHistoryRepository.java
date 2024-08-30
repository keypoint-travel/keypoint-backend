package com.keypoint.keypointtravel.campaign.repository;

import com.keypoint.keypointtravel.campaign.entity.EmailInvitationHistory;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface EmailInvitationHistoryRepository extends CrudRepository<EmailInvitationHistory, String> {

    List<EmailInvitationHistory> findByCampaignId(Long campaignId);
}
