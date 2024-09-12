package com.keypoint.keypointtravel.banner.dto.dto;

import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ManagementAdvDetailDto {

    private Long bannerId;
    private String mainTitle;
    private String subTitle;
    private String content;
    private String detailImageUrl;
    private LanguageCode languageCode;
}
