package com.keypoint.keypointtravel.theme.repository;

import com.keypoint.keypointtravel.global.dto.useCase.PageUseCase;
import com.keypoint.keypointtravel.theme.dto.response.ThemeResponse;
import org.springframework.data.domain.Page;

public interface ThemeCustomRepository {
    Page<ThemeResponse> findThemes(PageUseCase useCase);

    Page<ThemeResponse> findPaidThemes(PageUseCase useCase);
}
