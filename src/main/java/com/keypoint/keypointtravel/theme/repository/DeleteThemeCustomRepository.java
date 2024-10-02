package com.keypoint.keypointtravel.theme.repository;

import com.keypoint.keypointtravel.theme.dto.useCase.DeleteThemeUseCase;

public interface DeleteThemeCustomRepository {

    void deleteThemes(DeleteThemeUseCase useCase);

    void deletePaidThemes(DeleteThemeUseCase useCase);

}
