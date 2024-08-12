package com.keypoint.keypointtravel.guide.dto.response.readGuideDetailInAdmin;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReadGuideTranslationInAdminResponse {

    private Long guideTranslationId;
    private String title;
    private String subTitle;
    private String content;
}
