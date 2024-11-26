package com.keypoint.keypointtravel.requestPush.dto.useCase;

import com.keypoint.keypointtravel.global.enumType.member.RoleType;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.requestPush.dto.request.RequestPushRequest;
import com.keypoint.keypointtravel.requestPush.entity.RequestPush;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RequestPushUseCase {

    private RoleType roleType;

    private LanguageCode languageCode;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotNull
    private LocalDateTime reservationAt;

    public static RequestPushUseCase from(RequestPushRequest request) {
        return new RequestPushUseCase(
            request.getRoleType(),
            request.getLanguageCode(),
            request.getTitle(),
            request.getContent(),
            request.getReservationAt()
        );
    }

    public RequestPush toEntity() {
        return new RequestPush(
            title,
            content,
            languageCode,
            reservationAt,
            roleType
        );
    }

}
