package com.keypoint.keypointtravel.notification.dto.useCase;

import com.keypoint.keypointtravel.notification.dto.request.FCMTestRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreatePushNotificationUseCase {
    private Long memberId;
    private String title;
    private String body;

    public static CreatePushNotificationUseCase of(
            Long memberId,
            FCMTestRequest request
    ) {
        return new CreatePushNotificationUseCase(
                memberId,
                request.getTitle(),
                request.getBody()
        );
    }
}
