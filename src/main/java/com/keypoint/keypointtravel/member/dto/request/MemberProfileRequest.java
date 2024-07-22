package com.keypoint.keypointtravel.member.dto.request;

import com.keypoint.keypointtravel.global.annotation.ValidEnum;
import com.keypoint.keypointtravel.global.enumType.member.GenderType;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public class MemberProfileRequest {

    @NotBlank
    private String country;

    @ValidEnum(enumClass = GenderType.class)
    private GenderType gender;

    @NotNull
    private LocalDate birth;

    @NotBlank
    private String name;

    @ValidEnum(enumClass = LanguageCode.class)
    private LanguageCode languageCode;
}
