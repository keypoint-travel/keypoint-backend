package com.keypoint.keypointtravel.guide.dto.request;

import com.keypoint.keypointtravel.global.annotation.ValidEnum;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreateGuideTranslationRequest {

  @NotBlank
  private String title;
  @NotBlank
  private String subTitle;
  @NotBlank
  private String content;
  @ValidEnum(enumClass = LanguageCode.class)
  private LanguageCode languageCode;
}
