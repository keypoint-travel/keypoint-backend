package com.keypoint.keypointtravel.notification.event.pushNotification;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationType;
import java.util.List;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CampaignApplicantPushNotificationEvent extends PushNotificationEvent {

    private String applicantName;
    private Long campaignId;

    public CampaignApplicantPushNotificationEvent() {
        super(null, null);
    }

    public CampaignApplicantPushNotificationEvent(
        PushNotificationType type,
        List<Long> userIds,
        String applicantName,
        Long campaignId
    ) {
        super(type, userIds);
        this.applicantName = applicantName;
        this.campaignId = campaignId;
    }

    public static CampaignApplicantPushNotificationEvent of(
        PushNotificationType type,
        List<Long> userIds,
        String applicantName,
        Long campaignId
    ) {
        return new CampaignApplicantPushNotificationEvent(type, userIds, applicantName, campaignId);
    }

    @Override
    public Object getAdditionalData() {
        return new CampaignApplicantData(applicantName, campaignId);
    }

    @Getter
    public static class CampaignApplicantData {

        private String applicantName;
        private Long campaignId;

        public CampaignApplicantData(String applicantName, Long campaignId) {
            this.applicantName = applicantName;
            this.campaignId = campaignId;
        }
    }
}