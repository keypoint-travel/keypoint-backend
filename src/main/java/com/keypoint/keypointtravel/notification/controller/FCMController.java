package com.keypoint.keypointtravel.notification.controller;

import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.notification.dto.request.FCMTestRequest;
import com.keypoint.keypointtravel.notification.dto.useCase.CreatePushNotificationUseCase;
import com.keypoint.keypointtravel.notification.service.FCMService;
import com.keypoint.keypointtravel.notification.service.PushNotificationHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fcm")
public class FCMController {

    private final FCMService fcmService;
    private final PushNotificationHistoryService pushNotificationHistoryService;

    @GetMapping("/event")
    public ResponseEntity<?> testEvent() {
        //fcmService.testEvent();
        Locale aaa = LocaleContextHolder.getLocale();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/send")
    public APIResponseEntity<Void> sendFCM(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody FCMTestRequest request
    ) {

        Notification notification = Notification.builder()
            .setTitle(request.getTitle())
            .setBody(request.getBody())
            .build();
        Message message = Message.builder()
            .setNotification(notification)
            .setToken(request.getDeviceToken())
            .build();
        //FCMUtils.sendSingleMessage(message);

        CreatePushNotificationUseCase useCase = CreatePushNotificationUseCase.of(userDetails.getId(), request);
        pushNotificationHistoryService.savePushNotificationHistories(useCase);

        return APIResponseEntity.<Void>builder()
            .message("[테스트] FCM 전송 성공")
            .build();
    }
}
