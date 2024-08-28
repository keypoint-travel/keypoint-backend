package com.keypoint.keypointtravel.notification.event.pushNotification;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationType;

import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NoticePushNotificationEvent extends PushNotificationEvent {

    private NoticeData additionalData;

    public NoticePushNotificationEvent() {
        super(null, null);
    }

    public NoticePushNotificationEvent(
        PushNotificationType type,
        List<Long> memberIds,
        Long noticeId
    ) {
        super(type, memberIds);
        this.additionalData = new NoticeData(noticeId);
    }

    public static NoticePushNotificationEvent of(
        PushNotificationType type,
        List<Long> memberIds,
        Long noticeId
    ) {
        return new NoticePushNotificationEvent(type, memberIds, noticeId);
    }

    @Override
    public NoticeData getAdditionalData() {
        return additionalData;
    }

    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NoticeData {

        @JsonProperty("noticeId") 
        private Long noticeId;

        public NoticeData(Long noticeId) {
            this.noticeId = noticeId;
        }

        public Long getNoticeId() {
            return noticeId;
        }
    }
}
