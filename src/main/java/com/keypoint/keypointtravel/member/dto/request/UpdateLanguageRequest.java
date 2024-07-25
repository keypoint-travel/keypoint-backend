package com.keypoint.keypointtravel.member.dto.request;

import com.keypoint.keypointtravel.global.annotation.ValidEnum;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateLanguageRequest {

    @NotNull
    @ValidEnum(enumClass = LanguageCode.class)
    private LanguageCode language;
}
