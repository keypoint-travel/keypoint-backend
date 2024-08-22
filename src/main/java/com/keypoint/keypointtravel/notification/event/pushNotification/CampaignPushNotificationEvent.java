package com.keypoint.keypointtravel.notification.event.pushNotification;

import com.keypoint.keypointtravel.campaign.entity.Campaign;
import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationType;
import java.util.List;

public class CampaignPushNotificationEvent extends PushNotificationEvent {

    private Campaign campaign;

    public CampaignPushNotificationEvent(
        PushNotificationType type,
        List<Long> memberIds,
        Campaign campaign
    ) {
        super(type, memberIds);
        this.campaign = campaign;
    }

    public static CampaignPushNotificationEvent of(
        PushNotificationType type,
        List<Long> memberIds,
        Campaign campaign
    ) {
        return new CampaignPushNotificationEvent(type, memberIds, campaign);
    }

    @Override
    public Campaign getAdditionalData() {
        return this.campaign;
    }
}
