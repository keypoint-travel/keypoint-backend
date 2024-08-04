package com.keypoint.keypointtravel.notification.event.pushNotification;

import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationType;
import java.util.List;
import lombok.Getter;

public class FrendInvitePushNotificationEvent extends PushNotificationEvent {

    private String inviteeName;
    private String campaignName;


    public FrendInvitePushNotificationEvent(
        PushNotificationType type,
        List<Long> userIds,
        String campaignName,
        String inviteeName
    ) {
        super(type, userIds);
        this.campaignName = campaignName;
        this.inviteeName = inviteeName;
    }

    public static FrendInvitePushNotificationEvent of(
        PushNotificationType type,
        List<Long> userIds,
        String campaignName,
        String inviteeName
    ) {
        return new FrendInvitePushNotificationEvent(type, userIds, campaignName, inviteeName);
    }

    @Override
    public Object getAdditionalData() {
        return new FrendInviteData(campaignName, inviteeName);
    }

    @Getter
    public static class FrendInviteData {

        private String inviteeName;
        private String campaignName;

        public FrendInviteData(String inviteeName, String campaignName) {
            this.inviteeName = inviteeName;
            this.campaignName = campaignName;
        }
    }
}