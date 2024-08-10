package com.keypoint.keypointtravel.guide.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ReadGuideInAdminResponse {

  private Long guideId;
  private String title;
  private String subTitle;
  private String thumbnailImageUrl;
  private String content;
  private int order;
  private LocalDateTime modifiedAt;
}
