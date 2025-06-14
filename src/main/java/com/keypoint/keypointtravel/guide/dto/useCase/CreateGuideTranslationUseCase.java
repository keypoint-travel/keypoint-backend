package com.keypoint.keypointtravel.guide.dto.useCase;

import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.guide.dto.request.CreateGuideTranslationRequest;
import com.keypoint.keypointtravel.guide.entity.Guide;
import com.keypoint.keypointtravel.guide.entity.GuideTranslation;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateGuideTranslationUseCase {

    private Long guideId;
    private String title;
    private String subTitle;
    private String content;
    private LanguageCode languageCode;

    public static CreateGuideTranslationUseCase of(
        Long guideId,
        CreateGuideTranslationRequest request
    ) {
        return new CreateGuideTranslationUseCase(
            guideId,
            request.getTitle(),
            request.getSubTitle(),
            request.getContent(),
            request.getLanguageCode()
        );
    }

    public GuideTranslation toEntity(Guide guide) {
        return GuideTranslation.builder()
            .guide(guide)
            .title(this.title)
            .subTitle(this.subTitle)
            .content(this.content)
            .languageCode(this.languageCode)
            .build();
    }
}
