package com.keypoint.keypointtravel.notification.event.pushNotification;

import com.keypoint.keypointtravel.campaign.entity.Campaign;
import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationType;
import java.util.List;
import lombok.Getter;

public class CampaingApplicantPushNotificationEvent extends PushNotificationEvent {

    private String applicantName;
    private Campaign campaign;


    public CampaingApplicantPushNotificationEvent(
        PushNotificationType type,
        List<Long> userIds,
        String applicantName,
        Campaign campaign
    ) {
        super(type, userIds);
        this.applicantName = applicantName;
        this.campaign = campaign;
    }

    public static CampaingApplicantPushNotificationEvent of(
        PushNotificationType type,
        List<Long> userIds,
        String applicantName,
        Campaign campaign
    ) {
        return new CampaingApplicantPushNotificationEvent(type, userIds, applicantName, campaign);
    }

    @Override
    public Object getAdditionalData() {
        return new CampaignApplicantData(applicantName, campaign);
    }

    @Getter
    public static class CampaignApplicantData {

        private String applicantName;
        private Campaign campaign;

        public CampaignApplicantData(String applicantName, Campaign campaign) {
            this.applicantName = applicantName;
            this.campaign = campaign;
        }
    }
}