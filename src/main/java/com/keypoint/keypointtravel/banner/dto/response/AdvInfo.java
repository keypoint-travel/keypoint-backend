package com.keypoint.keypointtravel.banner.dto.response;

import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdvInfo {

    private LanguageCode languageCode;
    private String mainTitle;
    private String subTitle;
    private String content;
}
