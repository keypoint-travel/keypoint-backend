package com.keypoint.keypointtravel.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.keypoint.keypointtravel.notification.dto.dto.fcm.FCMSendDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FCMService {

    private final FirebaseMessaging firebaseMessaging;

    /**
     * FCM을 보낼 객체를 생성하는 함수
     *
     * @param dto
     * @return
     */
    public static Message makeMessage(FCMSendDTO dto) {
        Notification notification = Notification
            .builder()
            .setTitle(dto.getTitle())
            .setBody(dto.getBody())
            .build();
        
        return Message
            .builder()
            .setNotification(notification)
            .setToken(dto.getToken())
            .build();
    }

    /**
     * 푸시 메시지 처리를 수행하는 비즈니스 로직
     *
     * @param fcmSendDto fcm 보낼 데이터
     */
    public void sendMessageTo(FCMSendDTO fcmSendDto) throws FirebaseMessagingException {
        firebaseMessaging.send(
            makeMessage(fcmSendDto));
    }
}
