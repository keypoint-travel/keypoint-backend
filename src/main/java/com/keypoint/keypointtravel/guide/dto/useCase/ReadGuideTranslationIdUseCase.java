package com.keypoint.keypointtravel.guide.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReadGuideTranslationIdUseCase {

    private Long guideTranslationIds;

    public static ReadGuideTranslationIdUseCase from(Long guideTranslationIds) {
        return new ReadGuideTranslationIdUseCase(guideTranslationIds);
    }
}
