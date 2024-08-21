package com.keypoint.keypointtravel.guide.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeleteGuideTranslationUseCase {

    private Long guideId;
    private Long[] guideTranslationIds;


    public static DeleteGuideTranslationUseCase of(Long guideId, Long[] ids) {
        return new DeleteGuideTranslationUseCase(guideId, ids);
    }
}
