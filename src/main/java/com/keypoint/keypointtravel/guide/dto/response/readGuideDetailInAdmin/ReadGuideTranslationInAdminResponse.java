package com.keypoint.keypointtravel.guide.dto.response.readGuideDetailInAdmin;

import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReadGuideTranslationInAdminResponse {

    private Long guideTranslationId;
    private LanguageCode languageCode;
    private String title;
    private String subTitle;
    private String content;
}
