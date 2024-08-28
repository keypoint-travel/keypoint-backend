package com.keypoint.keypointtravel.notification.controller;

import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.global.utils.FCMUtils;
import com.keypoint.keypointtravel.notification.dto.request.FCMTestRequest;
import com.keypoint.keypointtravel.notification.service.FCMService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fcm")
public class FCMController {

    private final FCMService fcmService;

    @GetMapping("/event")
    public ResponseEntity<?> testEvent() {
        fcmService.testEvent();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/send")
    public APIResponseEntity<Void> sendFCM(
        @RequestBody FCMTestRequest request) {

        Notification notification = Notification.builder()
            .setTitle(request.getTitle())
            .setBody(request.getBody())
            .build();
        Message message = Message.builder()
            .setNotification(notification)
            .setToken(request.getDeviceToken())
            .build();
        FCMUtils.sendSingleMessage(message);

        return APIResponseEntity.<Void>builder()
            .message("[테스트] FCM 전송 성공")
            .build();
    }
}
