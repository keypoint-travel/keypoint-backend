package com.keypoint.keypointtravel.member.dto.request;

import com.keypoint.keypointtravel.global.annotation.ValidEnum;
import com.keypoint.keypointtravel.global.enumType.member.GenderType;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public class MemberProfileRequest {

    private String country;

    @ValidEnum(enumClass = GenderType.class, nullable = true)
    private GenderType gender;

    private LocalDate birth;

    @NotBlank
    private String name;

    @ValidEnum(enumClass = LanguageCode.class)
    private LanguageCode languageCode;
}
