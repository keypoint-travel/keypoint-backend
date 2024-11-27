package com.keypoint.keypointtravel.requestPush.dto.useCase;

import com.keypoint.keypointtravel.global.annotation.ValidEnum;
import com.keypoint.keypointtravel.global.enumType.member.RoleType;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class RequestPushUseCase {

    @ValidEnum(nullable = true, enumClass = RoleType.class)
    private RoleType roleType;

    @ValidEnum(enumClass = LanguageCode.class)
    private LanguageCode languageCode;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotNull
    private LocalDateTime reservationAt;
}
