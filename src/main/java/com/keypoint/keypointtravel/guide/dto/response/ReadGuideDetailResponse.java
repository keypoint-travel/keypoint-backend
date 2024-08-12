package com.keypoint.keypointtravel.guide.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReadGuideDetailResponse {

    private Long guideTranslationIds;
    private String title;
    private String subTitle;
    private String thumbnailImageUrl;
    private String content;
}
