package com.keypoint.keypointtravel.guide.dto.request.updateGuide;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateGuideTranslationRequest {

  @NotBlank
  private String title;

  @NotBlank
  private String subTitle;

  @NotBlank
  private String content;
}
