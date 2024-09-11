package com.keypoint.keypointtravel.notification.controller;

import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.global.enumType.error.CommonErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.global.utils.FCMUtils;
import com.keypoint.keypointtravel.notification.dto.request.FCMTestRequest;
import com.keypoint.keypointtravel.notification.dto.response.FCMBodyResponse;
import com.keypoint.keypointtravel.notification.dto.useCase.CreatePushNotificationUseCase;
import com.keypoint.keypointtravel.notification.service.FCMService;
import com.keypoint.keypointtravel.notification.service.PushNotificationHistoryService;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fcm")
public class FCMController {

    private final FCMService fcmService;
    private final PushNotificationHistoryService pushNotificationHistoryService;

    @GetMapping("/event")
    public ResponseEntity<?> testEvent() {
        //fcmService.testEvent();
        Locale currentLocale = LocaleContextHolder.getLocale();
        throw new GeneralException(CommonErrorCode.FAIL_TO_DELETE_EN_DATA);
        //return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/send")
    public APIResponseEntity<Void> sendFCM(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody FCMTestRequest request
    ) {
        FCMBodyResponse response = FCMBodyResponse.of(request.getBody());
        Notification notification = Notification.builder()
            .setTitle(request.getTitle())
            .setBody(response.toString())
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
