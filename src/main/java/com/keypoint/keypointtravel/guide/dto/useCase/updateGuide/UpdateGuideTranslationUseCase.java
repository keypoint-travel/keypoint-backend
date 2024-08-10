package com.keypoint.keypointtravel.guide.dto.useCase.updateGuide;

import com.keypoint.keypointtravel.guide.dto.request.updateGuide.UpdateGuideTranslationRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateGuideTranslationUseCase {

  private Long guideTranslationId;
  private String title;
  private String subTitle;
  private String content;

  public static UpdateGuideTranslationUseCase from(UpdateGuideTranslationRequest request) {
    return new UpdateGuideTranslationUseCase(
        request.getGuideTranslationId(),
        request.getTitle(),
        request.getSubTitle(),
        request.getContent()
    );
  }
}
