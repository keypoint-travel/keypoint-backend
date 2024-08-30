package com.keypoint.keypointtravel.guide.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReadGuideTranslationIdUseCase {

    private Long memberId;
    private Long guideTranslationIds;

    public static ReadGuideTranslationIdUseCase of(Long memberId, Long guideTranslationIds) {
        return new ReadGuideTranslationIdUseCase(memberId, guideTranslationIds);
    }
}
