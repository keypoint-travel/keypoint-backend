package com.keypoint.keypointtravel.notification.event.pushNotification;

import com.keypoint.keypointtravel.campaign.entity.Campaign;
import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationType;
import java.util.List;
import lombok.Getter;

public class CampaingAcceptorPushNotificationEvent extends PushNotificationEvent {

    private String acceptorName;
    private Campaign campaign;

    public CampaingAcceptorPushNotificationEvent() {
        super(null, null);
    }

    public CampaingAcceptorPushNotificationEvent(
        PushNotificationType type,
        List<Long> userIds,
        String acceptorName,
        Campaign campaign
    ) {
        super(type, userIds);
        this.acceptorName = acceptorName;
        this.campaign = campaign;
    }

    public static CampaingAcceptorPushNotificationEvent of(
        PushNotificationType type,
        List<Long> userIds,
        String acceptorName,
        Campaign campaign
    ) {
        return new CampaingAcceptorPushNotificationEvent(type, userIds, acceptorName, campaign);
    }

    @Override
    public Object getAdditionalData() {
        return new CampaignAcceptorData(acceptorName, campaign);
    }

    @Getter
    public static class CampaignAcceptorData {

        private String acceptorName;
        private Campaign campaign;

        public CampaignAcceptorData(String leaderName, Campaign campaign) {
            this.acceptorName = leaderName;
            this.campaign = campaign;
        }
    }
}