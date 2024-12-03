package com.keypoint.keypointtravel.requestPush.service;

import com.keypoint.keypointtravel.global.enumType.error.CommonErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.requestPush.dto.useCase.RequestAlarmUseCase;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommonRequestAlarmService {

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

}
