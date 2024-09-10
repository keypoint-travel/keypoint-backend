package com.keypoint.keypointtravel.theme.service;

import com.keypoint.keypointtravel.global.enumType.error.ThemeErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.theme.dto.useCase.CreateThemeUseCase;
import com.keypoint.keypointtravel.theme.dto.useCase.DeleteThemeUseCase;
import com.keypoint.keypointtravel.theme.dto.useCase.UpdateThemeUseCase;
import com.keypoint.keypointtravel.theme.entity.PaidTheme;
import com.keypoint.keypointtravel.theme.repository.PaidThemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaidThemeService {
    private final PaidThemeRepository paidThemeRepository;

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
            long result = paidThemeRepository.updatePaidTheme(useCase);
            if (result < 0) {
                throw new GeneralException(ThemeErrorCode.NOT_EXISTED_Theme);
            }
            else{
                // 3. 차트 컬러 업데이트
                theme.updateChartColors(useCase.getChartColors());
                paidThemeRepository.save(theme);
            }

        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }



}
