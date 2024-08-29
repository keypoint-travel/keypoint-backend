package com.keypoint.keypointtravel.guide.service;

import com.keypoint.keypointtravel.global.constants.DirectoryConstants;
import com.keypoint.keypointtravel.global.enumType.error.CommonErrorCode;
import com.keypoint.keypointtravel.global.enumType.error.GuideErrorCode;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.guide.dto.useCase.updateGuide.UpdateGuideTranslationUseCase;
import com.keypoint.keypointtravel.guide.dto.useCase.updateGuide.UpdateGuideUseCase;
import com.keypoint.keypointtravel.guide.entity.Guide;
import com.keypoint.keypointtravel.guide.repository.GuideRepository;
import com.keypoint.keypointtravel.guide.repository.GuideTranslationRepository;
import com.keypoint.keypointtravel.uploadFile.service.UploadFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UpdateGuideService {

    private final GuideRepository guideRepository;
    private final GuideTranslationRepository guideTranslationRepository;
    private final ReadGuideService readGuideService;
    private final UploadFileService uploadFileService;

    /**
     * 이용 가이드 업데이트
     *
     * @param useCase
     */
    @Transactional
    public void updateGuide(UpdateGuideUseCase useCase) {
        try {
            Long guideId = useCase.getGuideId();

            // 1. 유효성 검사
            if (guideRepository.existsByIdNotAndOrderAndIsDeletedFalse(
                guideId,
                useCase.getOrder()
            )) {
                throw new GeneralException(CommonErrorCode.DUPLICATED_ORDER);
            }

            // 2. 썸네일 업데이트
            Guide guide = readGuideService.findGuideByGuideId(guideId);
            uploadFileService.updateUploadFile(
                guide.getThumbnailImageId(),
                useCase.getThumbnailImage(),
                DirectoryConstants.GUIDE_THUMBNAIL_DIRECTORY
            );

            // 3. 업데이트
            long result = guideRepository.updateGuide(useCase);
            if (result < 0) {
                throw new GeneralException(GuideErrorCode.NOT_EXISTED_GUIDE);
            }
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }

    /**
     * 이용 가이드 번역물 업데이트
     *
     * @param useCase
     */
    public void updateGuideTranslation(UpdateGuideTranslationUseCase useCase) {
        try {
            // 1. 유효성 검사
            if (guideTranslationRepository.existsByIdNotAndLanguageCodeAndIsDeletedFalse(
                // 이미 존재하는 언어 코드인지 확인
                useCase.getGuideId(),
                useCase.getGuideTranslationId(),
                useCase.getLanguageCode()
            )) {
                throw new GeneralException(GuideErrorCode.DUPLICATED_GUIDE_TRANSLATION_LANGUAGE);
            }
            if (guideTranslationRepository.existsByIdAndLanguageCodeAndIsDeletedFalse(
                // 영어버전 변경을 시도하는 건지 확인
                useCase.getGuideTranslationId(),
                LanguageCode.EN
            ) && useCase.getLanguageCode() != LanguageCode.EN) {
                throw new GeneralException(CommonErrorCode.FAIL_TO_DELETE_EN_DATA);
            }

            // 2. 업데이트
            long result = guideRepository.updateGuideTranslation(useCase);
            if (result < 0) {
                throw new GeneralException(GuideErrorCode.NOT_EXISTED_GUIDE);
            }
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }
}
