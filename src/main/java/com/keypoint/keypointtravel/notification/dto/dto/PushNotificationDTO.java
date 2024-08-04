package com.keypoint.keypointtravel.notification.dto.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PushNotificationDTO {

    private String title;
    private String body;

    public static PushNotificationDTO of(String title, String body) {
        return new PushNotificationDTO(title, body);
    }
}
