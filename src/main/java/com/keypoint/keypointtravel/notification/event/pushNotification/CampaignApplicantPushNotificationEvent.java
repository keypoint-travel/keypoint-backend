package com.keypoint.keypointtravel.notification.event.pushNotification;

import com.keypoint.keypointtravel.campaign.entity.Campaign;
import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationType;
import java.util.List;
import lombok.Getter;

public class CampaignApplicantPushNotificationEvent extends PushNotificationEvent {

    private String applicantName;
    private Campaign campaign;

    public CampaignApplicantPushNotificationEvent() {
        super(null, null);
    }

    public CampaignApplicantPushNotificationEvent(
        PushNotificationType type,
        List<Long> userIds,
        String applicantName,
        Campaign campaign
    ) {
        super(type, userIds);
        this.applicantName = applicantName;
        this.campaign = campaign;
    }

    public static CampaignApplicantPushNotificationEvent of(
        PushNotificationType type,
        List<Long> userIds,
        String applicantName,
        Campaign campaign
    ) {
        return new CampaignApplicantPushNotificationEvent(type, userIds, applicantName, campaign);
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