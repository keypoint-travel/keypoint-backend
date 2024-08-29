package com.keypoint.keypointtravel.guide.service;

import com.keypoint.keypointtravel.global.enumType.error.CommonErrorCode;
import com.keypoint.keypointtravel.global.enumType.error.GuideErrorCode;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.guide.dto.useCase.DeleteGuideGuideUseCase;
import com.keypoint.keypointtravel.guide.dto.useCase.DeleteGuideTranslationUseCase;
import com.keypoint.keypointtravel.guide.repository.GuideRepository;
import com.keypoint.keypointtravel.guide.repository.GuideTranslationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DeleteGuideService {

    private final GuideRepository guideRepository;
    private final GuideTranslationRepository guideTranslationRepository;

    /**
     * 이용 가이드 삭제 함수
     *
     * @param useCase
     */
    @Transactional
    public void deleteGuides(DeleteGuideGuideUseCase useCase) {
        try {
            guideRepository.deleteGuides(useCase);
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }

    /**
     * 이용 가이드 번역물 삭제 함수
     *
     * @param useCase
     */
    @Transactional
    public void deleteGuideTranslations(DeleteGuideTranslationUseCase useCase) {
        try {
            // 1. 유효성 검사
            if (guideTranslationRepository.existsByIdAndLanguageCodeAndIsDeletedFalse(
                useCase.getGuideTranslationId(),
                LanguageCode.EN
            )) {
                throw new GeneralException(CommonErrorCode.FAIL_TO_DELETE_EN_DATA);
            }

            // 2. 삭제
            long result = guideRepository.deleteGuideTranslations(useCase);

            if (result < 0) {
                throw new GeneralException(GuideErrorCode.NOT_EXISTED_GUIDE);
            }
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }
}
