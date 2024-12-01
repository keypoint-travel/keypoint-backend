package com.keypoint.keypointtravel.requestPush.repository;

import com.keypoint.keypointtravel.global.dto.useCase.PageUseCase;
import com.keypoint.keypointtravel.requestPush.dto.response.RequestPushAlarmResponse;
import com.keypoint.keypointtravel.requestPush.dto.useCase.UpdateRequestPushAlarmUseCase;
import org.springframework.data.domain.Page;

public interface RequestPushCustomRepository {

    long updateRequestPushAlarm(UpdateRequestPushAlarmUseCase useCase);

    Page<RequestPushAlarmResponse> findRequestPushAlarms(PageUseCase useCase);
}
