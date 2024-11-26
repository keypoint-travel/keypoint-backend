package com.keypoint.keypointtravel.requestPush.dto.useCase;

import com.keypoint.keypointtravel.global.enumType.member.RoleType;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.requestPush.dto.request.CreateRequestPushRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateRequestPushUseCase {

    private RoleType roleType;

    private LanguageCode languageCode;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotNull
    private LocalDateTime reservationAt;

    public static CreateRequestPushUseCase from(CreateRequestPushRequest request) {
        return new CreateRequestPushUseCase(
            request.getRoleType(),
            request.getLanguageCode(),
            request.getTitle(),
            request.getContent(),
            request.getReservationAt()
        );
    }

}
