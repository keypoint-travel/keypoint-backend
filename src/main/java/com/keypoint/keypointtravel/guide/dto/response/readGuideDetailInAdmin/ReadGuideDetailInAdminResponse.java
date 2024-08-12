package com.keypoint.keypointtravel.guide.dto.response.readGuideDetailInAdmin;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReadGuideDetailInAdminResponse {

    private Long guideId;
    private String thumbnailImageUrl;
    private int order;
    private List<ReadGuideTranslationInAdminResponse> translations;
}
