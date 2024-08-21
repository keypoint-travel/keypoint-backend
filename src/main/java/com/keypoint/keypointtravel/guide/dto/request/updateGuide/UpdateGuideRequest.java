package com.keypoint.keypointtravel.guide.dto.request.updateGuide;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;

@Getter
public class UpdateGuideRequest {

  @Min(0)
  private int order;

  @NotNull
  private List<UpdateGuideTranslationRequest> translations;
}
