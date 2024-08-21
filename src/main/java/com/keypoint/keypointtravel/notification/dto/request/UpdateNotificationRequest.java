package com.keypoint.keypointtravel.notification.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateNotificationRequest {

    @NotNull
    private boolean notificationEnabled;
}
