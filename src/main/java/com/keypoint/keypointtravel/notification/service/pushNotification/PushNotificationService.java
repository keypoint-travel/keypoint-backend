package com.keypoint.keypointtravel.notification.service.pushNotification;

import com.keypoint.keypointtravel.notification.event.pushNotification.PushNotificationEvent;

public interface PushNotificationService {

    /**
     * FCM 전송
     */
    void sendNotification(PushNotificationEvent event);
}
