package com.keypoint.keypointtravel.theme.repository;

import com.keypoint.keypointtravel.theme.dto.useCase.UpdateThemeUseCase;

public interface UpdateThemeCustomRepository {

    long updateTheme(UpdateThemeUseCase useCase);

    long updatePaidTheme(UpdateThemeUseCase useCase);

}
