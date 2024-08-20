package com.keypoint.keypointtravel.campaign.repository;

import com.keypoint.keypointtravel.campaign.dto.dto.EmailInvitationHistory;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface EmailInvitationHistoryRepository extends CrudRepository<EmailInvitationHistory, String> {

    List<EmailInvitationHistory> findByCampaignId(Long campaignId);
}
