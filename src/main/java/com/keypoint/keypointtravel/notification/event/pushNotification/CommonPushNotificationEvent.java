package com.keypoint.keypointtravel.notification.event.pushNotification;

import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationType;
import java.util.List;

public class CommonPushNotificationEvent extends PushNotificationEvent {

    public CommonPushNotificationEvent(
        PushNotificationType type,
        List<Long> memberIds
    ) {
        super(type, memberIds);
    }

    public static CommonPushNotificationEvent of(
        PushNotificationType type,
        List<Long> memberIds
    ) {
        return new CommonPushNotificationEvent(type, memberIds);
    }

    @Override
    public Object getAdditionalData() {
        return null;
    }

}
