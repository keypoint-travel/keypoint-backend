package com.keypoint.keypointtravel.notification.event.pushNotification;

import com.keypoint.keypointtravel.campaign.entity.Campaign;
import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationType;
import java.util.List;
import lombok.Getter;

public class CampaignLeaderPushNotificationEvent extends PushNotificationEvent {

    private String leaderName;
    private Campaign campaign;

    public CampaignLeaderPushNotificationEvent() {
        super(null, null);
    }

    public CampaignLeaderPushNotificationEvent(
        PushNotificationType type,
        List<Long> userIds,
        String leaderName,
        Campaign campaign
    ) {
        super(type, userIds);
        this.leaderName = leaderName;
        this.campaign = campaign;
    }

    public static CampaignLeaderPushNotificationEvent of(
        PushNotificationType type,
        List<Long> userIds,
        String leaderName,
        Campaign campaign
    ) {
        return new CampaignLeaderPushNotificationEvent(type, userIds, leaderName, campaign);
    }

    @Override
    public Object getAdditionalData() {
        return new CampaignLeaderData(leaderName, campaign);
    }

    @Getter
    public static class CampaignLeaderData {

        private String leaderName;
        private Campaign campaign;

        public CampaignLeaderData(String leaderName, Campaign campaign) {
            this.leaderName = leaderName;
            this.campaign = campaign;
        }
    }
}