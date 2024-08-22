package com.keypoint.keypointtravel.notification.event.pushNotification;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationType;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CampaignLeaderPushNotificationEvent extends PushNotificationEvent {

    private CampaignLeaderData additionalData;

    public CampaignLeaderPushNotificationEvent() {
        super(null, null);
    }

    public CampaignLeaderPushNotificationEvent(
        PushNotificationType type,
        List<Long> userIds,
        String leaderName,
        Long campaignId
    ) {
        super(type, userIds);
        this.additionalData = new CampaignLeaderData(leaderName, campaignId);
    }

    public static CampaignLeaderPushNotificationEvent of(
        PushNotificationType type,
        List<Long> userIds,
        String leaderName,
        Long campaignId
    ) {
        return new CampaignLeaderPushNotificationEvent(type, userIds, leaderName, campaignId);
    }
    @Override
    public Object getAdditionalData() {
        return additionalData;
    }

    @Getter
    @NoArgsConstructor
    public static class CampaignLeaderData {

        private String leaderName;
        private Long campaignId;

        public CampaignLeaderData(String leaderName, Long campaignId) {
            this.leaderName = leaderName;
            this.campaignId = campaignId;
        }
    }
}