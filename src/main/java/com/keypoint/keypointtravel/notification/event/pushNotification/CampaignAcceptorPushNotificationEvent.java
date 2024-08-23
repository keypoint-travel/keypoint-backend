package com.keypoint.keypointtravel.notification.event.pushNotification;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationType;
import java.util.List;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CampaignAcceptorPushNotificationEvent extends PushNotificationEvent {

    private String acceptorName;
    private Long campaignId;

    public CampaignAcceptorPushNotificationEvent() {
        super(null, null);
    }

    public CampaignAcceptorPushNotificationEvent(
        PushNotificationType type,
        List<Long> userIds,
        String acceptorName,
        Long campaignId
    ) {
        super(type, userIds);
        this.acceptorName = acceptorName;
        this.campaignId = campaignId;
    }

    public static CampaignAcceptorPushNotificationEvent of(
        PushNotificationType type,
        List<Long> userIds,
        String acceptorName,
        Long campaignId
    ) {
        return new CampaignAcceptorPushNotificationEvent(type, userIds, acceptorName, campaignId);
    }

    @Override
    public Object getAdditionalData() {
        return new CampaignAcceptorData(acceptorName, campaignId);
    }

    @Getter
    public static class CampaignAcceptorData {

        private String acceptorName;
        private Long campaignId;

        public CampaignAcceptorData(String leaderName, Long campaignId) {
            this.acceptorName = leaderName;
            this.campaignId = campaignId;
        }
    }
}