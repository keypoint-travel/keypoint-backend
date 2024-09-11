package com.keypoint.keypointtravel.notification.dto.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FCMBodyResponse {

    private PushNotificationType type;
    private String content;
    private Object detail;

    @Override
    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static FCMBodyResponse of(
        String content
    ) {
        return new FCMBodyResponse(
            PushNotificationType.PUSH_NOTIFICATION_BY_ADMIN,
            content,
            null
        );
    }
}
