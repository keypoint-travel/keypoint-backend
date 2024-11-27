package com.keypoint.keypointtravel.notification.event.pushNotification;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationType;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AdminPushNotificationEvent extends PushNotificationEvent {

    private FCMContentData additionalData;

    public AdminPushNotificationEvent() {
        super(null, null);
    }

    public AdminPushNotificationEvent(
        PushNotificationType type,
        String title,
        String content,
        List<Long> memberIds
    ) {
        super(type, memberIds);
        this.additionalData = new FCMContentData(title, content);
    }

    public AdminPushNotificationEvent(
            PushNotificationType type,
            String title,
            String content
    ) {
        super(type, null);
        this.additionalData = new FCMContentData(title, content);
    }

    public static AdminPushNotificationEvent of(
        PushNotificationType type,
        String title,
        String content,
        List<Long> memberIds
    ) {
        return new AdminPushNotificationEvent(type, title, content, memberIds);
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