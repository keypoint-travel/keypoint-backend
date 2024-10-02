package com.keypoint.keypointtravel.banner.dto.request;

import com.keypoint.keypointtravel.global.annotation.ValidEnum;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BannerListRequest {

    private String region;
    private String tourType;
    private String cat1;
    private String cat2;
    private String cat3;
    private int page;
    private int size;
    @ValidEnum(enumClass = LanguageCode.class)
    private LanguageCode languageCode;
}
