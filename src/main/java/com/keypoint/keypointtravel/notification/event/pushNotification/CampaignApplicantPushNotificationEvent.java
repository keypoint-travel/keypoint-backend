package com.keypoint.keypointtravel.notification.event.pushNotification;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationType;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CampaignApplicantPushNotificationEvent extends PushNotificationEvent {

    private CampaignApplicantData additionalData;

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
        this.additionalData = new CampaignApplicantData(applicantName, campaignId);
    }

    public String getApplicantName() {
        return additionalData != null ? additionalData.getApplicantName() : null;
    }

    public Long getCampaignId() {
        return additionalData != null ? additionalData.getCampaignId() : null;
    }

    @Override
    public Object getAdditionalData() {
        return additionalData;
    }

    @Getter
    @NoArgsConstructor
    public static class CampaignApplicantData {
        private String applicantName;
        private Long campaignId;

        public CampaignApplicantData(String applicantName, Long campaignId) {
            this.applicantName = applicantName;
            this.campaignId = campaignId;
        }
    }
}