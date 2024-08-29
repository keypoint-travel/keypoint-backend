package com.keypoint.keypointtravel.guide.dto.request.updateGuide;

import com.keypoint.keypointtravel.global.annotation.ValidEnum;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateGuideTranslationRequest {

  @ValidEnum(enumClass = LanguageCode.class)
  private LanguageCode languageCode;

  @NotBlank
  private String title;

  @NotBlank
  private String subTitle;

  @NotBlank
  private String content;
}
