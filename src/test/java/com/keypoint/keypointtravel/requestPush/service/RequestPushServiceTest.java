package com.keypoint.keypointtravel.requestPush.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.keypoint.keypointtravel.global.enumType.error.CommonErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.global.factory.RequestPushFactory;
import com.keypoint.keypointtravel.requestPush.dto.useCase.RequestAlarmUseCase;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("[비즈니스 로직] - RequestPushServiceTest")
class RequestPushServiceTest {

    @InjectMocks
    private RequestPushAlarmService requestPushService;

    @Test
    @DisplayName("[정상 케이스] 푸시 요청 유효성 검사를 성공한다.")
    void testValidateRequestPush_success() {
        // give
        RequestAlarmUseCase useCase = RequestPushFactory.createCreateRequestPushUseCase();

        // when & then
        assertDoesNotThrow(() -> requestPushService.validateRequestPush(useCase));
    }

    @Test
    @DisplayName("[오류 케이스 - INVALID_REQUEST_DATA] 30분 간격이 아님으로 푸시 요청 유효성 검사를 실패한다.")
    void testValidateRequestPush_reservationAtIsNot30MinuteInterval() {
        // given
        LocalDateTime invalidTime = LocalDateTime.now().plusHours(1).withMinute(15);
        RequestAlarmUseCase useCase = RequestPushFactory.createCreateRequestPushUseCase(
            invalidTime);

        // when
        GeneralException ex =
            assertThrows(
                GeneralException.class, () -> requestPushService.validateRequestPush(useCase));

        // then
        assertEquals(CommonErrorCode.INVALID_REQUEST_DATA, ex.getErrorCode());
    }

    @Test
    @DisplayName("[오류 케이스 - INVALID_REQUEST_DATA] 과거 시간을 요청하여 푸시 요청 유효성 검사를 실패한다.")
    void testValidateRequestPush_reservationAtIsInThePast() {
        // given
        LocalDateTime invalidTime = LocalDateTime.now().plusHours(1).withMinute(15);
        RequestAlarmUseCase useCase = RequestPushFactory.createCreateRequestPushUseCase(
            invalidTime);

        // when
        GeneralException ex =
            assertThrows(
                GeneralException.class, () -> requestPushService.validateRequestPush(useCase));

        // then
        assertEquals(CommonErrorCode.INVALID_REQUEST_DATA, ex.getErrorCode());
    }
}