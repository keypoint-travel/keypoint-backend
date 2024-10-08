package com.keypoint.keypointtravel.notification.event.pushNotification;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationType;
import java.util.List;
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
        Long noticeContentId
    ) {
        super(type, memberIds);
        this.additionalData = new NoticeData(noticeContentId);
    }

    public static NoticePushNotificationEvent of(
        PushNotificationType type,
        List<Long> memberIds,
        Long noticeContentId
    ) {
        return new NoticePushNotificationEvent(type, memberIds, noticeContentId);
    }

    @Override
    public NoticeData getAdditionalData() {
        return additionalData;
    }

    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NoticeData {

        @JsonProperty("noticeContentId")
        private Long noticeContentId;

        public NoticeData(Long noticeContentId) {
            this.noticeContentId = noticeContentId;
        }

        public Long getNoticeContentId() {
            return noticeContentId;
        }
    }
}
