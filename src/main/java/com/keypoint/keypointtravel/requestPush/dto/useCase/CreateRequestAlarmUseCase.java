package com.keypoint.keypointtravel.requestPush.dto.useCase;

import com.keypoint.keypointtravel.global.enumType.member.RoleType;
import com.keypoint.keypointtravel.global.enumType.notification.RequestAlarmType;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.requestPush.dto.request.RequestPushAlarmRequest;
import com.keypoint.keypointtravel.requestPush.entity.RequestAlarm;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CreateRequestAlarmUseCase extends RequestAlarmUseCase {

    private RequestAlarmType type;

    public CreateRequestAlarmUseCase(
        RoleType roleType,
        LanguageCode languageCode,
        String title,
        String content,
        LocalDateTime reservationAt,
        RequestAlarmType type
    ) {
        super(roleType, languageCode, title, content, reservationAt);
        this.type = type;
    }

    public static CreateRequestAlarmUseCase of(RequestPushAlarmRequest request,
        RequestAlarmType type) {
        return new CreateRequestAlarmUseCase(
            request.getRoleType(),
            request.getLanguageCode(),
            request.getTitle(),
            request.getContent(),
            request.getReservationAt(),
            type
        );
    }

    public RequestAlarm toEntity() {
        return new RequestAlarm(
            this.getTitle(),
            this.getContent(),
            this.getLanguageCode(),
            this.getReservationAt(),
            this.getRoleType(),
            type
        );
    }
}
