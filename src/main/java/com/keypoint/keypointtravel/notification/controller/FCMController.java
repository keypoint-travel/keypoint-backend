package com.keypoint.keypointtravel.notification.controller;

import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.global.utils.FCMUtils;
import com.keypoint.keypointtravel.notification.dto.request.FCMTestRequest;
import com.keypoint.keypointtravel.notification.dto.useCase.CreatePushNotificationUseCase;
import com.keypoint.keypointtravel.notification.service.FCMService;
import com.keypoint.keypointtravel.notification.service.PushNotificationHistoryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fcm")
public class FCMController {

    private final FCMService fcmService;
    private final PushNotificationHistoryService pushNotificationHistoryService;

    @GetMapping("/event")
    public ResponseEntity<?> testEvent() {
        //fcmService.testEvent();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        //localeConfig.resolveLocale(request);
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
        FCMUtils.sendSingleMessage(message);

        CreatePushNotificationUseCase useCase = CreatePushNotificationUseCase.of(userDetails.getId(), request);
        pushNotificationHistoryService.savePushNotificationHistories(useCase);

        return APIResponseEntity.<Void>builder()
            .message("[테스트] FCM 전송 성공")
            .build();
    }
}
