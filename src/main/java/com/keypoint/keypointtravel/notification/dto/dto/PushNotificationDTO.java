package com.keypoint.keypointtravel.notification.dto.dto;

import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationContent;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PushNotificationDTO {

    private PushNotificationContent pushNotificationContent;
    private String title;
    private String body;

    public static PushNotificationDTO of(
        PushNotificationContent pushNotificationContent,
        String title,
        String body
    ) {
        return new PushNotificationDTO(
            pushNotificationContent,
            title,
            body
        );
    }
}
