package com.keypoint.keypointtravel.requestPush.service;

import com.keypoint.keypointtravel.global.dto.useCase.PageUseCase;
import com.keypoint.keypointtravel.requestPush.dto.response.RequestAlarmResponse;
import com.keypoint.keypointtravel.requestPush.dto.useCase.CreateRequestAlarmUseCase;
import com.keypoint.keypointtravel.requestPush.dto.useCase.DeleteRequestAlarmUseCase;
import com.keypoint.keypointtravel.requestPush.dto.useCase.UpdateRequestAlarmUseCase;
import org.springframework.data.domain.Page;

public interface RequestAlarmService {

    /**
     * RequestAlarm 생성
     *
     * @param useCase 생성할 데이터
     */
    void addRequestAlarm(CreateRequestAlarmUseCase useCase);

    /**
     * RequestAlarm 업데이트
     *
     * @param useCase 수정할 데이터
     */
    void updateRequestAlarm(UpdateRequestAlarmUseCase useCase);

    /**
     * 요청 알림 조회
     *
     * @param useCase 페이지네이션 정보
     * @return
     */
    Page<RequestAlarmResponse> findRequestAlarms(PageUseCase useCase);

    /**
     * 요청 알림 삭제
     *
     * @param useCase 삭제할 요청 알림 아이디
     */
    void deleteRequestAlarm(DeleteRequestAlarmUseCase useCase);
}
