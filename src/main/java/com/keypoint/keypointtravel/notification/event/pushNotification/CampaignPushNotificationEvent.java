package com.keypoint.keypointtravel.notification.event.pushNotification;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationType;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CampaignPushNotificationEvent extends PushNotificationEvent {

    private Long campaignId;

    public CampaignPushNotificationEvent() {
        super(null, null);
    }

    public CampaignPushNotificationEvent(
        PushNotificationType type,
        List<Long> memberIds,
        Long campaignId
    ) {
        super(type, memberIds);
        this.campaignId = campaignId;
    }

    public static CampaignPushNotificationEvent of(
        PushNotificationType type,
        List<Long> memberIds,
        Long campaignId
    ) {
        return new CampaignPushNotificationEvent(type, memberIds, campaignId);
    }

    @Override
    public Long getAdditionalData() {
        return this.campaignId;
    }
}
