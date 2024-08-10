package com.keypoint.keypointtravel.guide.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReadGuideDetailResponse {

  private Long guideTranslationIds;
  private String title;
  private String subTitle;
  private String thumbnailImageUrl;
  private String content;
}
