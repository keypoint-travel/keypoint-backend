package com.keypoint.keypointtravel.notification.repository.fcmToken;

import com.keypoint.keypointtravel.notification.dto.useCase.FCMTokenUseCase;

public interface FCMTokenCustomRepository {

    long deleteFCMTokenByTokenAndMemberId(FCMTokenUseCase useCase);
}
