package com.keypoint.keypointtravel.notification.repository.fcmToken;

import com.keypoint.keypointtravel.notification.dto.useCase.FCMTokenUseCase;
import java.util.List;

public interface FCMTokenCustomRepository {

    long deleteFCMTokenByTokenAndMemberId(FCMTokenUseCase useCase);

    long deleteFCMTokenByTokens(List<String> tokens);
}
