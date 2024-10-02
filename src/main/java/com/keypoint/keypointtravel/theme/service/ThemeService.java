package com.keypoint.keypointtravel.theme.service;

import com.keypoint.keypointtravel.global.dto.useCase.PageUseCase;
import com.keypoint.keypointtravel.global.enumType.error.ThemeErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.theme.dto.response.ThemeResponse;
import com.keypoint.keypointtravel.theme.dto.useCase.CreateThemeUseCase;
import com.keypoint.keypointtravel.theme.dto.useCase.DeleteThemeUseCase;
import com.keypoint.keypointtravel.theme.dto.useCase.UpdateThemeUseCase;
import com.keypoint.keypointtravel.theme.entity.Theme;
import com.keypoint.keypointtravel.theme.entity.ThemeColor;
import com.keypoint.keypointtravel.theme.repository.ThemeRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ThemeService {

    private final ThemeRepository themeRepository;
    @Transactional
    public void saveTheme(CreateThemeUseCase useCase){
        try {
            // 1. 무료 테마 저장
            Theme theme = useCase.toThemeEntity(useCase);

            List<ThemeColor> chartColors = useCase.getChartColors().stream()
                .map(chartColor -> ThemeColor.builder()
                    .theme(theme)
                    .color(chartColor)
                    .build())
                .collect(Collectors.toList());

            theme.updateChartColors(chartColors);

            themeRepository.save(theme);

        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }

    /**
     * 테마 삭제 성공
     *
     * @param useCase
     */
    public void deleteThemes(DeleteThemeUseCase useCase) {
        try {
            themeRepository.deleteThemes(useCase);
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }

    /**
     * 테마 수정
     *
     * @param useCase
     */
    @Transactional
    public void updateTheme(UpdateThemeUseCase useCase) {
        try {
            Long themeId = useCase.getThemeId();

            // 1. 유효성 검사
            Theme theme = themeRepository.findByIdAndIsDeletedFalse(themeId)
                .orElseThrow(() -> new GeneralException(ThemeErrorCode.NOT_EXISTED_Theme));

            // 2. 업데이트
            long updatedCount = themeRepository.updateTheme(useCase);

            if (updatedCount > 0) {
                Theme themeEntity = themeRepository.findById(useCase.getThemeId())
                    .orElseThrow(() -> new GeneralException(ThemeErrorCode.NOT_EXISTED_Theme));

                themeEntity.getChartColors().clear();
                List<ThemeColor> newColors = useCase.getChartColors().stream()
                    .map(colorCode -> new ThemeColor(themeEntity, null, colorCode))
                    .collect(Collectors.toList());

                themeEntity.updateChartColors(newColors);

                themeRepository.save(themeEntity);
            }
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }

    public Page<ThemeResponse> findThemes(PageUseCase useCase) {
        try {
            return themeRepository.findThemes(useCase);
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }


}
