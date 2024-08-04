package com.keypoint.keypointtravel.notification.event.pushNotification;

import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationType;
import java.util.List;
import lombok.Getter;

public class CampaignInvitePushNotificationEvent extends PushNotificationEvent {

    private String inviteeName;
    private String campaignName;


    public CampaignInvitePushNotificationEvent(
        PushNotificationType type,
        List<Long> userIds,
        String campaignName,
        String inviteeName
    ) {
        super(type, userIds);
        this.campaignName = campaignName;
        this.inviteeName = inviteeName;
    }

    public static CampaignInvitePushNotificationEvent of(
        PushNotificationType type,
        List<Long> userIds,
        String campaignName,
        String inviteeName
    ) {
        return new CampaignInvitePushNotificationEvent(type, userIds, campaignName, inviteeName);
    }

    @Override
    public Object getAdditionalData() {
        return new CampaignInviteData(campaignName, inviteeName);
    }

    @Getter
    public static class CampaignInviteData {

        private String inviteeName;
        private String campaignName;

        public CampaignInviteData(String inviteeName, String campaignName) {
            this.inviteeName = inviteeName;
            this.campaignName = campaignName;
        }
    }
}