package com.keypoint.keypointtravel.notification.event.pushNotification;

import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationType;
import java.util.List;

public class CampaignPushNotificationEvent extends PushNotificationEvent {

    private String campaignName;

    public CampaignPushNotificationEvent(
        PushNotificationType type,
        List<Long> memberIds,
        String campaignName
    ) {
        super(type, memberIds);
        this.campaignName = campaignName;
    }

    public static CampaignPushNotificationEvent of(
        PushNotificationType type,
        List<Long> memberIds,
        String campaignName
    ) {
        return new CampaignPushNotificationEvent(type, memberIds, campaignName);
    }

    @Override
    public String getAdditionalData() {
        return this.campaignName;
    }
}
