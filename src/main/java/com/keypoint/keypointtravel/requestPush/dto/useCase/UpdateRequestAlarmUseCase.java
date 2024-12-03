package com.keypoint.keypointtravel.requestPush.dto.useCase;

import com.keypoint.keypointtravel.global.enumType.member.RoleType;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.requestPush.dto.request.RequestPushAlarmRequest;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class UpdateRequestAlarmUseCase extends RequestAlarmUseCase {

    private Long requestPushId;

    public UpdateRequestAlarmUseCase(
        Long requestPushId,
        RoleType roleType,
        LanguageCode languageCode,
        String title,
        String content,
        LocalDateTime reservationAt
    ) {
        super(roleType, languageCode, title, content, reservationAt);
        this.requestPushId = requestPushId;
    }

    public static UpdateRequestAlarmUseCase of(
        Long requestPushId,
        RequestPushAlarmRequest request
    ) {
        return new UpdateRequestAlarmUseCase(
            requestPushId,
            request.getRoleType(),
            request.getLanguageCode(),
            request.getTitle(),
            request.getContent(),
            request.getReservationAt()
        );
    }
}
