package com.keypoint.keypointtravel.guide.dto.response.readGuideDetail;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReadGuideDetailResponse {

    private Long guideTranslationIds;
    private String title;
    private String subTitle;
    private int order;
    private String thumbnailImageUrl;
    private String content;
    private ReadNextGuideResponse nextGuide;

    public void setNextGuide(ReadNextGuideResponse nextGuide) {
        this.nextGuide = nextGuide;
    }
}
