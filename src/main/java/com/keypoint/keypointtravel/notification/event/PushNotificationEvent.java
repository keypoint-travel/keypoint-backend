package com.keypoint.keypointtravel.notification.event;

import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PushNotificationEvent {

    private PushNotificationType pushNotificationType; // 푸시 알림 타입

    private Long userId; // 푸시 알림을 보낼 사용자 id

    public static PushNotificationEvent of(PushNotificationType pushNotificationType, Long userId) {
        return new PushNotificationEvent(pushNotificationType, userId);
    }
}
