package com.keypoint.keypointtravel.notification.repository.notification;

import com.keypoint.keypointtravel.notification.dto.useCase.UpdateNotificationUseCase;

public interface NotificationCustomRepository {

    void updateNotification(UpdateNotificationUseCase useCase);
}
