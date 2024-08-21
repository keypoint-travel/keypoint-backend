package com.keypoint.keypointtravel.notification.dto.useCase;

import com.keypoint.keypointtravel.global.enumType.notification.AlarmType;
import com.keypoint.keypointtravel.notification.dto.request.UpdateNotificationRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateNotificationUseCase {

    private Long memberId;
    private AlarmType alarmType;
    private boolean notificationEnabled;

    public static UpdateNotificationUseCase of(
        Long memberId,
        AlarmType alarmType,
        UpdateNotificationRequest request
    ) {
        return new UpdateNotificationUseCase(
            memberId,
            alarmType,
            request.isNotificationEnabled()
        );
    }
}
