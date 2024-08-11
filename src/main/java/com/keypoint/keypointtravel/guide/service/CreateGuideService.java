package com.keypoint.keypointtravel.guide.service;

import com.keypoint.keypointtravel.global.constants.DirectoryConstants;
import com.keypoint.keypointtravel.global.enumType.error.GuideErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.guide.dto.useCase.CreateGuideTranslationUseCase;
import com.keypoint.keypointtravel.guide.dto.useCase.CreateGuideUseCase;
import com.keypoint.keypointtravel.guide.entity.Guide;
import com.keypoint.keypointtravel.guide.entity.GuideTranslation;
import com.keypoint.keypointtravel.guide.repository.GuideRepository;
import com.keypoint.keypointtravel.guide.repository.GuideTranslationRepository;
import com.keypoint.keypointtravel.uploadFile.service.UploadFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CreateGuideService {

    private final GuideRepository guideRepository;
    private final GuideTranslationRepository guideTranslationRepository;
    private final ReadGuideService readGuideService;
    private final UploadFileService uploadFileService;

    /**
     * 이용 가이드 추가함수 - 처음 등록은 영어 버전으로 등록
     *
     * @param useCase
     */
    @Transactional
    public void addGuide(CreateGuideUseCase useCase) {
        try {
            // 1. 유효성 검사
            if (readGuideService.validateOrder(useCase.getOrder())) {
                throw new GeneralException(GuideErrorCode.DUPLICATED_ORDER);
            }

            // 2. 썸네일 저장
            Long thumbnailId = uploadFileService.saveUploadFile(
                useCase.getThumbnailImage(),
                DirectoryConstants.GUIDE_THUMBNAIL_DIRECTORY);

            // 3. 이용 가이드 저장 (Guide & GuideTranslation)
            Guide guide = useCase.toGuideEntity(thumbnailId);
            GuideTranslation guideTranslation = useCase.toGuideTranslationEntity();

            guideRepository.save(guide);
            guideTranslationRepository.save(guideTranslation);
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }

    /**
     * 이용 가이드 다른 언어 버전 번역물 등록 함수
     *
     * @param useCase
     */
    @Transactional
    public void addGuideTranslation(CreateGuideTranslationUseCase useCase) {
        try {

        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }
}
