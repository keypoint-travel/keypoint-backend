package com.keypoint.keypointtravel.notification.event.pushNotification;

import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationType;
import java.util.List;

public class AnnouncementPushNotificationEvent extends PushNotificationEvent {

    private String announcementName;

    public AnnouncementPushNotificationEvent() {
        super(null, null);
    }

    public AnnouncementPushNotificationEvent(
        PushNotificationType type,
        List<Long> memberIds,
        String campaignName
    ) {
        super(type, memberIds);
        this.announcementName = campaignName;
    }

    public static AnnouncementPushNotificationEvent of(
        PushNotificationType type,
        List<Long> memberIds,
        String campaignName
    ) {
        return new AnnouncementPushNotificationEvent(type, memberIds, campaignName);
    }

    @Override
    public String getAdditionalData() {
        return this.announcementName;
    }
}

