package com.keypoint.keypointtravel.notification.event.pushNotification;

import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationType;
import java.util.List;

public class FriendPushNotificationEvent extends PushNotificationEvent {

    private String friendName;

    public FriendPushNotificationEvent() {
        super(null, null);
    }
    

    public FriendPushNotificationEvent(
        PushNotificationType type,
        List<Long> memberIds,
        String friendName
    ) {
        super(type, memberIds);
        this.friendName = friendName;
    }

    public static FriendPushNotificationEvent of(
        PushNotificationType type,
        List<Long> memberIds,
        String friendName
    ) {
        return new FriendPushNotificationEvent(type, memberIds, friendName);
    }

    @Override
    public String getAdditionalData() {
        return this.friendName;
    }
}
