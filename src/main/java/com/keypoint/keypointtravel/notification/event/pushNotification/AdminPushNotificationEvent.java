package com.keypoint.keypointtravel.notification.event.pushNotification;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AdminPushNotificationEvent extends PushNotificationEvent {

    private FCMContentData additionalData;

    public AdminPushNotificationEvent() {
        super(null, null);
    }

    public AdminPushNotificationEvent(
            PushNotificationType type,
            List<Long> userIds,
            String title,
            String content
    ) {
        super(type, userIds);
        this.additionalData = new FCMContentData(title, content);
    }

    public String getTitle() {
        return additionalData != null ? additionalData.getTitle() : null;
    }

    public String getContent() {
        return additionalData != null ? additionalData.getContent() : null;
    }

    @Override
    public Object getAdditionalData() {
        return additionalData;
    }

    @Getter
    @NoArgsConstructor
    public static class FCMContentData {
        private String title;
        private String content;

        public FCMContentData(String title, String content) {
            this.title = title;
            this.content = content;
        }
    }
}