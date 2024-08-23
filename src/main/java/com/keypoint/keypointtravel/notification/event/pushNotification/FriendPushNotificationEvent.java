package com.keypoint.keypointtravel.notification.event.pushNotification;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationType;
import java.util.List;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FriendPushNotificationEvent extends PushNotificationEvent {

    @JsonProperty("additionalData")
    private FriendData additionalData;

    public FriendPushNotificationEvent() {
        super(null, null);
    }

    public FriendPushNotificationEvent(
        PushNotificationType type,
        List<Long> memberIds,
        String friendName
    ) {
        super(type, memberIds);
        this.additionalData = new FriendData(friendName);
    }

    public static FriendPushNotificationEvent of(
        PushNotificationType type,
        List<Long> memberIds,
        String friendName
    ) {
        return new FriendPushNotificationEvent(type, memberIds, friendName);
    }

    @Override
    public FriendData getAdditionalData() {
        return this.additionalData;
    }

    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FriendData {

        private String friendName;

        public FriendData(String friendName) {
            this.friendName = friendName;
        }

        public String getFriendName() {
            return friendName;
        }
    }
}
