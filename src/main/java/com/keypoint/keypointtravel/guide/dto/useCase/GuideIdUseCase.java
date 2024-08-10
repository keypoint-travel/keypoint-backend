package com.keypoint.keypointtravel.guide.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GuideIdUseCase {

  private Long guideId;

  public static GuideIdUseCase from(Long guideId) {
    return new GuideIdUseCase(guideId);
  }
}
