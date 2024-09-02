package com.keypoint.keypointtravel.guide.dto.request.updateGuide;

import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class UpdateGuideRequest {

  @Min(0)
  private int order;
}
