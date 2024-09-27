package com.keypoint.keypointtravel.theme.service;

import com.keypoint.keypointtravel.global.dto.useCase.PageUseCase;
import com.keypoint.keypointtravel.global.enumType.error.ThemeErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.theme.dto.response.ThemeResponse;
import com.keypoint.keypointtravel.theme.dto.useCase.CreateThemeUseCase;
import com.keypoint.keypointtravel.theme.dto.useCase.DeleteThemeUseCase;
import com.keypoint.keypointtravel.theme.dto.useCase.UpdateThemeUseCase;
import com.keypoint.keypointtravel.theme.entity.PaidTheme;
import com.keypoint.keypointtravel.theme.entity.ThemeColor;
import com.keypoint.keypointtravel.theme.repository.PaidThemeRepository;
import com.keypoint.keypointtravel.theme.repository.ThemeRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaidThemeService {
    private final PaidThemeRepository paidThemeRepository;
    private final ThemeRepository themeRepository;

    @Transactional
    public void savePaidTheme(CreateThemeUseCase useCase){
        try {
            // 1. 유료 테마 저장
            PaidTheme paidTheme = useCase.toPaidThemeEntity(useCase);

            List<ThemeColor> chartColors = useCase.getChartColors().stream()
                .map(chartColor -> ThemeColor.builder()
                    .paidTheme(paidTheme)
                    .color(chartColor)
                    .build())
                .collect(Collectors.toList());

            paidTheme.updateChartColors(chartColors);

            paidThemeRepository.save(paidTheme);

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
            paidThemeRepository.deletePaidThemes(useCase);
            paidThemeRepository.flush();
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
            PaidTheme theme = paidThemeRepository.findByIdAndIsDeletedFalse(themeId)
                .orElseThrow(() -> new GeneralException(ThemeErrorCode.NOT_EXISTED_Theme));

            // 2. 업데이트
            long updatedCount = paidThemeRepository.updatePaidTheme(useCase);

            if (updatedCount > 0) {
                PaidTheme themeEntity = paidThemeRepository.findById(useCase.getThemeId())
                    .orElseThrow(() -> new GeneralException(ThemeErrorCode.NOT_EXISTED_Theme));

                themeEntity.getChartColors().clear();

                List<ThemeColor> newColors = useCase.getChartColors().stream()
                    .map(colorCode -> new ThemeColor(null, themeEntity, colorCode))
                    .collect(Collectors.toList());

                themeEntity.updateChartColors(newColors);

                paidThemeRepository.save(themeEntity);
            }
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }

    public Page<ThemeResponse> findThemes(PageUseCase useCase) {
        try {
            return themeRepository.findPaidThemes(useCase);
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }


}
