package com.keypoint.keypointtravel.guide.dto.request.updateGuide;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateGuideTranslationRequest {

  @NotNull
  private Long guideTranslationId;

  @NotBlank
  private String title;

  @NotBlank
  private String subTitle;

  @NotBlank
  private String content;
}
