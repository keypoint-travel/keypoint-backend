package com.keypoint.keypointtravel.guide.repository;

import com.keypoint.keypointtravel.guide.dto.useCase.DeleteGuideGuideUseCase;
import com.keypoint.keypointtravel.guide.dto.useCase.DeleteGuideTranslationUseCase;
import com.keypoint.keypointtravel.guide.dto.useCase.updateGuide.UpdateGuideUseCase;

public interface UpdateGuideCustomRepository {

    void updateGuide(UpdateGuideUseCase useCase);

    void deleteGuides(DeleteGuideGuideUseCase useCase);

    void deleteGuideTranslations(DeleteGuideTranslationUseCase useCase);
}
