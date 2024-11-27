package com.keypoint.keypointtravel.requestPush.dto.useCase;

import com.keypoint.keypointtravel.global.enumType.member.RoleType;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.requestPush.dto.request.RequestPushRequest;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class UpdateRequestPushUseCase extends RequestPushUseCase {

    @NotNull
    private Long requestPushId;

    public UpdateRequestPushUseCase(
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

    public static UpdateRequestPushUseCase of(
        Long requestPushId,
        RequestPushRequest request
    ) {
        return new UpdateRequestPushUseCase(
            requestPushId,
            request.getRoleType(),
            request.getLanguageCode(),
            request.getTitle(),
            request.getContent(),
            request.getReservationAt()
        );
    }
}
