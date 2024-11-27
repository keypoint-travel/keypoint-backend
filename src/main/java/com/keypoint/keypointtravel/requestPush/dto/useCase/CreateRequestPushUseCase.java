package com.keypoint.keypointtravel.requestPush.dto.useCase;

import com.keypoint.keypointtravel.global.enumType.member.RoleType;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.requestPush.dto.request.RequestPushRequest;
import com.keypoint.keypointtravel.requestPush.entity.RequestPush;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CreateRequestPushUseCase extends RequestPushUseCase {

    public CreateRequestPushUseCase(
        RoleType roleType,
        LanguageCode languageCode,
        String title,
        String content,
        LocalDateTime reservationAt
    ) {
        super(roleType, languageCode, title, content, reservationAt);
    }

    public static CreateRequestPushUseCase from(RequestPushRequest request) {
        return new CreateRequestPushUseCase(
            request.getRoleType(),
            request.getLanguageCode(),
            request.getTitle(),
            request.getContent(),
            request.getReservationAt()
        );
    }

    public RequestPush toEntity() {
        return new RequestPush(
            this.getTitle(),
            this.getContent(),
            this.getLanguageCode(),
            this.getReservationAt(),
            this.getRoleType()
        );
    }
}
