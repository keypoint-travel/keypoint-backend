package com.keypoint.keypointtravel.theme.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.keypoint.keypointtravel.theme.dto.useCase.UpdateThemeUseCase;

public interface UpdateThemeCustomRepository {

    long updateTheme(UpdateThemeUseCase useCase);

    long updatePaidTheme(UpdateThemeUseCase useCase) throws JsonProcessingException;

}
