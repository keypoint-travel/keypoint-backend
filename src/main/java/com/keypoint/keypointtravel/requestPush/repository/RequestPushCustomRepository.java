package com.keypoint.keypointtravel.requestPush.repository;

import com.keypoint.keypointtravel.global.dto.useCase.PageUseCase;
import com.keypoint.keypointtravel.requestPush.dto.response.RequestPushResponse;
import com.keypoint.keypointtravel.requestPush.dto.useCase.UpdateRequestPushUseCase;
import org.springframework.data.domain.Page;

public interface RequestPushCustomRepository {

    long updateRequestPush(UpdateRequestPushUseCase useCase);

    Page<RequestPushResponse> findRequestPushes(PageUseCase useCase);
}
