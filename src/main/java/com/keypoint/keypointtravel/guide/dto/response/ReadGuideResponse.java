package com.keypoint.keypointtravel.guide.dto.response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ReadGuideResponse {

  private Long guideTranslationIds;
  private String title;
  private String subTitle;
  private String thumbnailImageUrl;
  private int order;
}
