package com.keypoint.keypointtravel.banner.dto.useCase;

import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FindAdvertisementUseCase {

    private LanguageCode languageCode;
    private Long bannerId;
}
