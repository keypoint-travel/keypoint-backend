package com.keypoint.keypointtravel.notification.service;

import com.keypoint.keypointtravel.notification.entity.PushNotificationHistory;
import com.keypoint.keypointtravel.notification.repository.pushNotificationHistory.PushNotificationHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PushNotificationHistoryService {

    private final PushNotificationHistoryRepository pushNotificationHistoryRepository;


    @Transactional
    public void savePushNotificationHistory(PushNotificationHistory history) {
        pushNotificationHistoryRepository.save(history);
    }
}
