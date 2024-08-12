package com.keypoint.keypointtravel.guide.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReadGuideResponse {

    private Long guideTranslationIds;
    private String title;
    private String subTitle;
    private String thumbnailImageUrl;
    private int order;
}
