package com.keypoint.keypointtravel.notification.controller;

import com.keypoint.keypointtravel.global.annotation.ValidEnum;
import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.global.enumType.notification.AlarmType;
import com.keypoint.keypointtravel.member.dto.response.IsExistedResponse;
import com.keypoint.keypointtravel.member.dto.useCase.MemberIdUseCase;
import com.keypoint.keypointtravel.notification.dto.request.FCMTokenRequest;
import com.keypoint.keypointtravel.notification.dto.request.UpdateNotificationRequest;
import com.keypoint.keypointtravel.notification.dto.useCase.FCMTokenUseCase;
import com.keypoint.keypointtravel.notification.dto.useCase.UpdateNotificationUseCase;
import com.keypoint.keypointtravel.notification.service.FCMTokenService;
import com.keypoint.keypointtravel.notification.service.NotificationService;
import com.keypoint.keypointtravel.notification.service.PushNotificationHistoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/alarms")
public class NotificationController {

    private final NotificationService notificationService;
    private final FCMTokenService fcmTokenService;
    private final PushNotificationHistoryService pushNotificationHistoryService;

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PatchMapping("")
    public APIResponseEntity<Void> updateNotification(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @Valid @ValidEnum(enumClass = AlarmType.class) @RequestParam(value = "alarm-type") AlarmType alarmType,
        @Valid @RequestBody UpdateNotificationRequest request) {
        UpdateNotificationUseCase useCase = UpdateNotificationUseCase.of(
            userDetails.getId(),
            alarmType,
            request
        );
        notificationService.updateNotification(useCase);

        return APIResponseEntity.<Void>builder()
            .message("알림 상태 업데이트 성공")
            .data(null)
            .build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PostMapping("/fcm-token")
    public APIResponseEntity<Void> addFCMToken(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @Valid @RequestBody FCMTokenRequest request) {
        FCMTokenUseCase useCase = FCMTokenUseCase.of(
            userDetails.getId(),
            request
        );
        fcmTokenService.addFCMToken(useCase);

        return APIResponseEntity.<Void>builder()
            .message("FCM 토큰 등록 성공")
            .data(null)
            .build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @DeleteMapping("/fcm-token")
    public APIResponseEntity<Void> deleteFCMToken(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @Valid @RequestBody FCMTokenRequest request) {
        FCMTokenUseCase useCase = FCMTokenUseCase.of(
            userDetails.getId(),
            request
        );
        fcmTokenService.deleteFCMToken(useCase);

        return APIResponseEntity.<Void>builder()
            .message("FCM 토큰 삭제 성공")
            .data(null)
            .build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("/api/v1/alarms/push")
    public APIResponseEntity<Void> findPushNotificationHistory(
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        MemberIdUseCase useCase = MemberIdUseCase.from(userDetails.getId());
        pushNotificationHistoryService.findPushNotificationHistory(useCase);

        return APIResponseEntity.<Void>builder()
            .message("푸시 알림 이력 조회 성공")
            .data(null)
            .build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("/push/unread")
    public APIResponseEntity<IsExistedResponse> checkIsExistedUnreadPushNotification(
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        MemberIdUseCase useCase = MemberIdUseCase.from(userDetails.getId());
        IsExistedResponse response = pushNotificationHistoryService.checkIsExistedUnreadPushNotification(
            useCase);

        return APIResponseEntity.<IsExistedResponse>builder()
            .message("읽지 않은 알림 존재 확인 성공")
            .data(response)
            .build();
    }
}
