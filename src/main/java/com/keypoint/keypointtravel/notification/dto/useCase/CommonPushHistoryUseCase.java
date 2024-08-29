package com.keypoint.keypointtravel.notification.dto.useCase;

import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationContent;
import com.keypoint.keypointtravel.notification.event.pushNotification.PushNotificationEvent;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CommonPushHistoryUseCase {

    private Long historyId;
    private PushNotificationContent type;
    private PushNotificationEvent detailData;
    private LocalDateTime arrivedAt;
    private boolean isRead;
}
