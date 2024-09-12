package com.keypoint.keypointtravel.notification.controller;

import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.keypoint.keypointtravel.badge.respository.BadgeRepository;
import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.global.enumType.notification.PushNotificationType;
import com.keypoint.keypointtravel.global.enumType.setting.BadgeType;
import com.keypoint.keypointtravel.notification.dto.request.FCMTestRequest;
import com.keypoint.keypointtravel.notification.dto.response.fcmBodyResponse.FCMBadgeDetailResponse;
import com.keypoint.keypointtravel.notification.dto.response.fcmBodyResponse.FCMBodyResponse;
import com.keypoint.keypointtravel.notification.dto.useCase.CreatePushNotificationUseCase;
import com.keypoint.keypointtravel.notification.service.FCMService;
import com.keypoint.keypointtravel.notification.service.PushNotificationHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fcm")
public class FCMController {

    private final FCMService fcmService;
    private final PushNotificationHistoryService pushNotificationHistoryService;
    private final BadgeRepository badgeRepository;

    @GetMapping("/event")
    public ResponseEntity<?> testEvent() {
        fcmService.testEvent();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/send")
    public APIResponseEntity<Void> sendFCM(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(value = "is-badge-earned", required = false) Boolean isBadgeEarned,
            @RequestBody FCMTestRequest request
    ) {
        // FCM Body 구성
        FCMBodyResponse response;
        if (isBadgeEarned != null) {
            String badgeUrl = badgeRepository.findByActiveBadgeUrl(BadgeType.FIRST_CAMPAIGN);
            response = FCMBodyResponse.of(
                    PushNotificationType.CAMPAIGN_END,
                    request.getBody(),
                    FCMBadgeDetailResponse.of(isBadgeEarned, badgeUrl)
            );
        } else {
            response = FCMBodyResponse.from(request.getBody());
        }

        // FCM 전송
        Notification notification = Notification.builder()
                .setTitle(request.getTitle())
                .setBody(response.toString())
                .build();
        Message message = Message.builder()
                .setNotification(notification)
                .setToken(request.getDeviceToken())
                .build();
        //FCMUtils.sendSingleMessage(message);

        // 이력 저장
        CreatePushNotificationUseCase useCase = CreatePushNotificationUseCase.of(userDetails.getId(), request);
        pushNotificationHistoryService.savePushNotificationHistories(useCase);

        return APIResponseEntity.<Void>builder()
            .message("[테스트] FCM 전송 성공")
            .build();
    }
}
