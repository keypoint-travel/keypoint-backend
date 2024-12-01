package com.keypoint.keypointtravel.requestPush.service;

import com.keypoint.keypointtravel.global.dto.useCase.PageUseCase;
import com.keypoint.keypointtravel.global.enumType.error.CommonErrorCode;
import com.keypoint.keypointtravel.global.enumType.error.NotificationErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.requestPush.dto.response.RequestPushAlarmResponse;
import com.keypoint.keypointtravel.requestPush.dto.useCase.CreateRequestPushAlarmUseCase;
import com.keypoint.keypointtravel.requestPush.dto.useCase.RequestAlarmUseCase;
import com.keypoint.keypointtravel.requestPush.dto.useCase.UpdateRequestPushAlarmUseCase;
import com.keypoint.keypointtravel.requestPush.entity.RequestAlarm;
import com.keypoint.keypointtravel.requestPush.repository.RequestPushRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestPushAlarmService {

    private final RequestPushRepository requestPushRepository;

    /**
     * RequestPush 유효성 검사
     *
     * @param useCase
     */
    public void validateRequestPush(RequestAlarmUseCase useCase) {
        LocalDateTime reservationAt = useCase.getReservationAt();

        // 현재 이후의 시간인지 확인
        if (reservationAt.isBefore(LocalDateTime.now())) {
            throw new GeneralException(CommonErrorCode.INVALID_REQUEST_DATA,
                "현재 이전의 시간을 예약할 수 없습니다.");
        }

        // 30분 단위인지
        if (reservationAt.getMinute() != 0 && reservationAt.getMinute() != 30) {
            throw new GeneralException(CommonErrorCode.INVALID_REQUEST_DATA,
                "30분 단위의 시간만 예약할 수 있습니다.");
        }
    }

    /**
     * RequestPush 생성
     *
     * @param useCase
     */
    @Transactional
    public void addRequestPushAlarm(CreateRequestPushAlarmUseCase useCase) {
        try {
            // 유효성 검사
            validateRequestPush(useCase);

            // 생성
            RequestAlarm requestPush = useCase.toEntity();
            requestPushRepository.save(requestPush);
        } catch (Exception e) {
            throw new GeneralException(e);
        }
    }

    /**
     * RequestPush 생성
     *
     * @param useCase
     */
    @Transactional
    public void updateRequestPushAlarm(UpdateRequestPushAlarmUseCase useCase) {
        try {
            // 유효성 검사
            validateRequestPush(useCase);

            // 수정
            long result = requestPushRepository.updateRequestPushAlarm(useCase);
            if (result < 1) {
                throw new GeneralException(NotificationErrorCode.NOT_EXISTED_REQUEST_PUSH);
            }
        } catch (GeneralException e) {
            throw new GeneralException(e);
        }
    }

    /**
     * 푸시 요청 조회 성공
     *
     * @param useCase
     * @return
     */
    public Page<RequestPushAlarmResponse> findRequestPushAlarms(PageUseCase useCase) {
        try {
            return requestPushRepository.findRequestPushAlarms(useCase);
        } catch (Exception e) {
            throw new GeneralException(e);
        }
    }
}
