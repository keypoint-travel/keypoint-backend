package com.keypoint.keypointtravel.banner.dto.useCase.advertisement;

import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EditLocaleAdvertisementUseCase {

    private LanguageCode language;
    private String mainTitle;
    private String subTitle;
    private String content;
}
