package com.keypoint.keypointtravel.notification.repository;

import com.keypoint.keypointtravel.notification.dto.useCase.UpdateNotificationUseCase;

public interface NotificationCustomRepository {

    void updateNotification(UpdateNotificationUseCase useCase);
}
