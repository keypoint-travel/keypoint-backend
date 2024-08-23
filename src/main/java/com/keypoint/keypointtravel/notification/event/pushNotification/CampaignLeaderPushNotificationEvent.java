package com.keypoint.keypointtravel.notification.event.pushNotification;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationType;
import java.util.List;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CampaignLeaderPushNotificationEvent extends PushNotificationEvent {

    private String leaderName;
    private Long campaignId;

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
        this.leaderName = leaderName;
        this.campaignId = campaignId;
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
        return new CampaignLeaderData(leaderName, campaignId);
    }

    @Getter
    public static class CampaignLeaderData {

        private String leaderName;
        private Long campaignId;

        public CampaignLeaderData(String leaderName, Long campaignId) {
            this.leaderName = leaderName;
            this.campaignId = campaignId;
        }
    }
}