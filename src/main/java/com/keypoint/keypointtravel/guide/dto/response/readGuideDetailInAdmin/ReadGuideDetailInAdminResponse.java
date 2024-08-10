package com.keypoint.keypointtravel.guide.dto.response.readGuideDetailInAdmin;

import java.util.List;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ReadGuideDetailInAdminResponse {

  private Long guideId;
  private String thumbnailImageUrl;
  private int order;
  private List<ReadGuideTranslationInAdminResponse> translations;
}
