package com.keypoint.keypointtravel.notification.dto.useCase;

import com.keypoint.keypointtravel.notification.dto.request.CreateFCMTokenRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateFCMTokenUseCase {

    private Long memberId;
    private String fcmToken;

    public static CreateFCMTokenUseCase of(
        Long memberId,
        CreateFCMTokenRequest request
    ) {
        return new CreateFCMTokenUseCase(memberId, request.getFcmToken());
    }
}
