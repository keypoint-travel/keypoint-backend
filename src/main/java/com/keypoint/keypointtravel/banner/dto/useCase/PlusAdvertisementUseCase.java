package com.keypoint.keypointtravel.banner.dto.useCase;

import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlusAdvertisementUseCase {

    private Long bannerId;
    private String mainTitle;
    private String subTitle;
    private String content;
    private LanguageCode language;
}
