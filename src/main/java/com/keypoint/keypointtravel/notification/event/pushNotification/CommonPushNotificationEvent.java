package com.keypoint.keypointtravel.notification.event.pushNotification;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationType;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonPushNotificationEvent extends PushNotificationEvent {

    public CommonPushNotificationEvent() {
        super(null, null);
    }

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
