package com.keypoint.keypointtravel.guide.repository;

import com.keypoint.keypointtravel.guide.dto.useCase.updateGuide.UpdateGuideUseCase;

public interface UpdateGuideCustomRepository {

    void updateGuide(UpdateGuideUseCase useCase);
}
