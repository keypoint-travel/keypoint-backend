package com.keypoint.keypointtravel.requestPush.dto.useCase;

import com.keypoint.keypointtravel.global.enumType.member.RoleType;
import com.keypoint.keypointtravel.global.enumType.notification.RequestAlarmType;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.requestPush.dto.request.RequestPushAlarmRequest;
import com.keypoint.keypointtravel.requestPush.entity.RequestAlarm;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CreateRequestPushAlarmUseCase extends RequestAlarmUseCase {

    public CreateRequestPushAlarmUseCase(
        RoleType roleType,
        LanguageCode languageCode,
        String title,
        String content,
        LocalDateTime reservationAt
    ) {
        super(roleType, languageCode, title, content, reservationAt);
    }

    public static CreateRequestPushAlarmUseCase from(RequestPushAlarmRequest request) {
        return new CreateRequestPushAlarmUseCase(
            request.getRoleType(),
            request.getLanguageCode(),
            request.getTitle(),
            request.getContent(),
            request.getReservationAt()
        );
    }

    public RequestAlarm toEntity() {
        return new RequestAlarm(
            this.getTitle(),
            this.getContent(),
            this.getLanguageCode(),
            this.getReservationAt(),
            this.getRoleType(),
            RequestAlarmType.PUSH
        );
    }
}
