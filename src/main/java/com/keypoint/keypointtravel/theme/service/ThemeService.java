package com.keypoint.keypointtravel.theme.service;

import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.theme.dto.useCase.CreateThemeUseCase;
import com.keypoint.keypointtravel.theme.entity.PaidTheme;
import com.keypoint.keypointtravel.theme.entity.Theme;
import com.keypoint.keypointtravel.theme.repository.PaidThemeRepository;
import com.keypoint.keypointtravel.theme.repository.ThemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final PaidThemeRepository paidThemeRepository;

    @Transactional
    public void saveTheme(CreateThemeUseCase useCase){
        try {
            // 1. 무료 테마 저장
            Theme theme = useCase.toThemeEntity(useCase);
            themeRepository.save(theme);

        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }

    @Transactional
    public void savePaidTheme(CreateThemeUseCase useCase){
        try {
            // 1. 유료 테마 저장
            PaidTheme paidTheme = useCase.toPaidThemeEntity(useCase);
            paidThemeRepository.save(paidTheme);

        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }



}
