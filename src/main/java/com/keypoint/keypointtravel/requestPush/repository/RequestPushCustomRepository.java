package com.keypoint.keypointtravel.requestPush.repository;

import com.keypoint.keypointtravel.global.dto.useCase.PageUseCase;
import com.keypoint.keypointtravel.requestPush.dto.response.RequestAlarmResponse;
import com.keypoint.keypointtravel.requestPush.dto.useCase.UpdateRequestAlarmUseCase;
import org.springframework.data.domain.Page;

public interface RequestPushCustomRepository {

    long updateRequestPushAlarm(UpdateRequestAlarmUseCase useCase);

    Page<RequestAlarmResponse> findRequestPushAlarms(PageUseCase useCase);
}
