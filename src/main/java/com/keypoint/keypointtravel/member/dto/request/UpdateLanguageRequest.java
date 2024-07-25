package com.keypoint.keypointtravel.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateLanguageRequest {

    @NotNull
    @ValidEnum(enumType=LanguageCode.class)
    private LanguageCode language;
}
