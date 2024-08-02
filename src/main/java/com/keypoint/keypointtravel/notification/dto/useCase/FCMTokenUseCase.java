package com.keypoint.keypointtravel.notification.dto.useCase;

import com.keypoint.keypointtravel.notification.dto.request.FCMTokenRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FCMTokenUseCase {

    private Long memberId;
    private String fcmToken;

    public static FCMTokenUseCase of(
        Long memberId,
        FCMTokenRequest request
    ) {
        return new FCMTokenUseCase(memberId, request.getFcmToken());
    }
}
