package com.keypoint.keypointtravel.campaign.repository;

import com.keypoint.keypointtravel.campaign.entity.EmailInvitationHistory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface EmailInvitationHistoryRepository extends CrudRepository<EmailInvitationHistory, String> {

    Optional<EmailInvitationHistory> findByCampaignIdAndEmail(Long campaignId, String email);

    List<EmailInvitationHistory> findAllByCampaignId(Long campaignId);
}
