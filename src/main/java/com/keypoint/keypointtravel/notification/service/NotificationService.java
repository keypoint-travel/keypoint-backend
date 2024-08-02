package com.keypoint.keypointtravel.notification.service;

import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.notification.dto.useCase.UpdateNotificationUseCase;
import com.keypoint.keypointtravel.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    /**
     * 알림 상태 업데이트 함수
     *
     * @param useCase 알림 변경 사항 데이터
     */
    public void updateNotification(UpdateNotificationUseCase useCase) {
        try {
            notificationRepository.updateNotification(useCase);
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }
}
