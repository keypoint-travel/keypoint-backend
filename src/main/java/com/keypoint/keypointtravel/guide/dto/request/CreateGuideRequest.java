package com.keypoint.keypointtravel.guide.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreateGuideRequest {

  @NotBlank
  private String title;
  @NotBlank
  private String subTitle;
  @NotBlank
  private String content;
  @Min(0)
  private int order;
}
