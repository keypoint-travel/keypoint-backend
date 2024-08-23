package com.keypoint.keypointtravel.notification.dto.dto;

import com.keypoint.keypointtravel.campaign.entity.Campaign;
import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationContent;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PushNotificationDTO {

    private PushNotificationContent pushNotificationMsg;
    private Campaign campaign;
    private String title;
    private String body;

    public static PushNotificationDTO of(
        PushNotificationContent pushNotificationMsg,
        String title,
        String body
    ) {
        return new PushNotificationDTO(
            pushNotificationMsg,
            null,
            title,
            body
        );
    }

    public static PushNotificationDTO of(
        PushNotificationContent pushNotificationMsg,
        Campaign campaign,
        String title,
        String body
    ) {
        return new PushNotificationDTO(
            pushNotificationMsg,
            campaign,
            title,
            body
        );
    }
}
