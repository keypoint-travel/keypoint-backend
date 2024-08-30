package com.keypoint.keypointtravel.guide.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeleteGuideGuideUseCase {

  private Long[] guideIds;

  public static DeleteGuideGuideUseCase from(Long[] guideId) {
    return new DeleteGuideGuideUseCase(guideId);
  }

  public static DeleteGuideGuideUseCase from(Long guideId) {
    return new DeleteGuideGuideUseCase(new Long[]{guideId});
  }
}
