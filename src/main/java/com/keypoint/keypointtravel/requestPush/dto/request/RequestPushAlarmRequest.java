package com.keypoint.keypointtravel.requestPush.dto.request;

import com.keypoint.keypointtravel.global.annotation.ValidEnum;
import com.keypoint.keypointtravel.global.enumType.member.RoleType;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class RequestPushAlarmRequest {

    @ValidEnum(enumClass = RoleType.class, nullable = true)
    private RoleType roleType;

    @ValidEnum(enumClass = LanguageCode.class, nullable = true)
    private LanguageCode languageCode;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotNull
    private LocalDateTime reservationAt;
}
