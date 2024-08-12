package com.keypoint.keypointtravel.guide.service;

import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.guide.dto.useCase.DeleteGuideGuideUseCase;
import com.keypoint.keypointtravel.guide.dto.useCase.DeleteGuideTranslationUseCase;
import com.keypoint.keypointtravel.guide.repository.GuideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DeleteGuideService {

    private final GuideRepository guideRepository;

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
            guideRepository.deleteGuideTranslations(useCase);
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }
}
