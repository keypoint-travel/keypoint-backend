package com.keypoint.keypointtravel.campaign.repository;

import com.keypoint.keypointtravel.campaign.dto.dto.CampaignDto;
import com.keypoint.keypointtravel.campaign.dto.dto.CampaignInfoDto;
import com.keypoint.keypointtravel.campaign.dto.dto.SendInvitationEmailDto;
import com.keypoint.keypointtravel.campaign.dto.dto.TravelLocationDto;
import com.keypoint.keypointtravel.campaign.dto.useCase.AlarmCampaignUseCase;
import com.keypoint.keypointtravel.global.enumType.campaign.Status;
import java.sql.Date;
import java.util.List;

public interface CustomCampaignRepository {

    SendInvitationEmailDto findSendInvitationEmailInfo(Long campaignId);

    CampaignInfoDto findCampaignInfo(Long campaignId);

    CampaignDto findCampaignInfoList(Long memberId, Status status, int size, int page);

    void updateCampaignFinished(Long campaignId);

    List<TravelLocationDto> findTravelLocationList(Long campaignId);

    boolean existsOverlappingCampaign(List<Long> memberIds, Date startDate, Date endDate);

    boolean existsOverlappingCampaign(List<Long> memberIds, Date startDate, Date endDate, Long campaignId);

    boolean existsOverlappingCampaign(List<Long> memberIds, Long campaignId);

    List<AlarmCampaignUseCase> findAlarmCampaignByStartAt(Date date);

    List<AlarmCampaignUseCase> findAlarmCampaignByStartAtAndNoExpense(Date date);

    List<AlarmCampaignUseCase> findAlarmCampaignByEndAt(Date date);
}
