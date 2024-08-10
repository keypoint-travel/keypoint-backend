package com.keypoint.keypointtravel.guide.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeleteGuideTranslationUseCase {

  private Long[] guideTranslationIds;

  public static DeleteGuideTranslationUseCase from(Long[] ids) {
    return new DeleteGuideTranslationUseCase(ids);
  }
}
