package com.keypoint.keypointtravel.global.factory;

import com.keypoint.keypointtravel.global.enumType.member.RoleType;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.requestPush.dto.useCase.RequestAlarmUseCase;
import java.time.LocalDateTime;

public class RequestPushFactory {

    public static RequestAlarmUseCase createCreateRequestPushUseCase() {
        LocalDateTime reservationAt = LocalDateTime.now().plusDays(3).withMinute(0);
        return new RequestAlarmUseCase(
            RoleType.ROLE_CERTIFIED_USER,
            LanguageCode.EN,
            "푸시 알림 제목",
            "푸시 알림 타이틀",
            reservationAt
        );
    }

    public static RequestAlarmUseCase createCreateRequestPushUseCase(
        LocalDateTime reservationAt) {
        return new RequestAlarmUseCase(
            RoleType.ROLE_CERTIFIED_USER,
            LanguageCode.EN,
            "푸시 알림 제목",
            "푸시 알림 타이틀",
            reservationAt
        );
    }
}
