package com.keypoint.keypointtravel.guide.dto.useCase.updateGuide;

import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.guide.dto.request.updateGuide.UpdateGuideTranslationRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateGuideTranslationUseCase {

  private Long guideId;
  private Long guideTranslationId;
  private LanguageCode languageCode;
  private String title;
  private String subTitle;
  private String content;

  public static UpdateGuideTranslationUseCase of(
      Long guideId,
      Long guideTranslationId,
      UpdateGuideTranslationRequest request
  ) {
    return new UpdateGuideTranslationUseCase(
        guideId,
        guideTranslationId,
        request.getLanguageCode(),
        request.getTitle(),
        request.getSubTitle(),
        request.getContent()
    );
  }
}
