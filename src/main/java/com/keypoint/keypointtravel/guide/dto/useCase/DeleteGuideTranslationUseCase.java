package com.keypoint.keypointtravel.guide.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeleteGuideTranslationUseCase {

    private Long guideId;
    private Long guideTranslationId;
    
    public static DeleteGuideTranslationUseCase of(Long guideId, Long guideTranslationId) {
        return new DeleteGuideTranslationUseCase(
            guideId,
            guideTranslationId
        );
    }
}
