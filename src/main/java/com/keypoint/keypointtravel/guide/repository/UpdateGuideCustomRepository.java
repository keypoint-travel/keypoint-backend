package com.keypoint.keypointtravel.guide.repository;

import com.keypoint.keypointtravel.guide.dto.useCase.DeleteGuideGuideUseCase;
import com.keypoint.keypointtravel.guide.dto.useCase.DeleteGuideTranslationUseCase;
import com.keypoint.keypointtravel.guide.dto.useCase.updateGuide.UpdateGuideTranslationUseCase;
import com.keypoint.keypointtravel.guide.dto.useCase.updateGuide.UpdateGuideUseCase;

public interface UpdateGuideCustomRepository {

    long updateGuide(UpdateGuideUseCase useCase);

    void deleteGuides(DeleteGuideGuideUseCase useCase);

    long deleteGuideTranslations(DeleteGuideTranslationUseCase useCase);

    long updateGuideTranslation(UpdateGuideTranslationUseCase useCase);
}
