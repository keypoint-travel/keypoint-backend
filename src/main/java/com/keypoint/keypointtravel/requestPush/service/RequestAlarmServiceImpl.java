package com.keypoint.keypointtravel.requestPush.service;

import com.keypoint.keypointtravel.global.dto.useCase.PageUseCase;
import com.keypoint.keypointtravel.global.enumType.error.NotificationErrorCode;
import com.keypoint.keypointtravel.global.enumType.notification.RequestAlarmType;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.requestPush.dto.response.RequestAlarmResponse;
import com.keypoint.keypointtravel.requestPush.dto.useCase.CreateRequestAlarmUseCase;
import com.keypoint.keypointtravel.requestPush.dto.useCase.DeleteRequestAlarmUseCase;
import com.keypoint.keypointtravel.requestPush.dto.useCase.UpdateRequestAlarmUseCase;
import com.keypoint.keypointtravel.requestPush.entity.RequestAlarm;
import com.keypoint.keypointtravel.requestPush.repository.RequestPushRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestAlarmServiceImpl implements RequestAlarmService {

    private final RequestPushRepository requestPushRepository;
    private final CommonRequestAlarmService commonRequestAlarmService;

    /**
     * RequestAlarm 생성
     *
     * @param useCase 생성할 데이터
     */
    @Override
    @Transactional
    public void addRequestAlarm(CreateRequestAlarmUseCase useCase) {
        try {
            // 유효성 검사
            commonRequestAlarmService.validateRequestPush(useCase);

            // 생성
            RequestAlarm requestPush = useCase.toEntity();
            requestPushRepository.save(requestPush);
        } catch (Exception e) {
            throw new GeneralException(e);
        }
    }

    /**
     * RequestAlarm 업데이트
     *
     * @param useCase 수정할 데이터
     */
    @Override
    @Transactional
    public void updateRequestAlarm(UpdateRequestAlarmUseCase useCase) {
        try {
            // 유효성 검사
            commonRequestAlarmService.validateRequestPush(useCase);

            // 수정
            long result = requestPushRepository.updateRequestAlarm(useCase);
            if (result < 1) {
                throw new GeneralException(NotificationErrorCode.NOT_EXISTED_REQUEST_PUSH);
            }
        } catch (GeneralException e) {
            throw new GeneralException(e);
        }
    }

    /**
     * 요청 알림 조회
     *
     * @param useCase 페이지네이션 정보
     * @return
     */
    @Override
    public Page<RequestAlarmResponse> findRequestAlarms(PageUseCase useCase,
        RequestAlarmType type
    ) {
        try {
            return requestPushRepository.findRequestAlarms(useCase, type);
        } catch (Exception e) {
            throw new GeneralException(e);
        }
    }

    /**
     * 요청 알림 삭제
     *
     * @param useCase 삭제할 요청 알림 아이디
     */
    @Override
    public void deleteRequestAlarm(DeleteRequestAlarmUseCase useCase) {
        try {
            requestPushRepository.deleteRequestAlarm(useCase);
        } catch (Exception e) {
            throw new GeneralException(e);
        }
    }
}
