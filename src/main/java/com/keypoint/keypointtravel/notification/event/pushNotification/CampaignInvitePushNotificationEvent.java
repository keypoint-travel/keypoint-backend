package com.keypoint.keypointtravel.notification.event.pushNotification;

import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationType;
import java.util.List;
import lombok.Getter;

public class CampaignInvitePushNotificationEvent extends PushNotificationEvent {

    private String register;
    private String campaignName;


    public CampaignInvitePushNotificationEvent(
        PushNotificationType type,
        List<Long> userIds,
        String campaignName,
        String register
    ) {
        super(type, userIds);
        this.campaignName = campaignName;
        this.register = register;
    }

    public static CampaignInvitePushNotificationEvent of(
        PushNotificationType type,
        List<Long> userIds,
        String campaignName,
        String register
    ) {
        return new CampaignInvitePushNotificationEvent(type, userIds, campaignName, register);
    }

    @Override
    public Object getAdditionalData() {
        return new CampaignInviteData(campaignName, register);
    }

    @Getter
    public static class CampaignInviteData {

        private String register;
        private String campaignName;

        public CampaignInviteData(String register, String campaignName) {
            this.register = register;
            this.campaignName = campaignName;
        }
    }
}