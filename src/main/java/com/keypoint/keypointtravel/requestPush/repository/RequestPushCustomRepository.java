package com.keypoint.keypointtravel.requestPush.repository;

import com.keypoint.keypointtravel.requestPush.dto.useCase.UpdateRequestPushUseCase;

public interface RequestPushCustomRepository {

    long updateRequestPush(UpdateRequestPushUseCase useCase);
}
