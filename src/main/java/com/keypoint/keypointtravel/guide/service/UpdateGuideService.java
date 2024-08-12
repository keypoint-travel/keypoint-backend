package com.keypoint.keypointtravel.guide.service;

import com.keypoint.keypointtravel.global.constants.DirectoryConstants;
import com.keypoint.keypointtravel.global.enumType.error.GuideErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.guide.dto.useCase.updateGuide.UpdateGuideUseCase;
import com.keypoint.keypointtravel.guide.entity.Guide;
import com.keypoint.keypointtravel.guide.repository.GuideRepository;
import com.keypoint.keypointtravel.uploadFile.service.UploadFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UpdateGuideService {

    private final GuideRepository guideRepository;
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
            if (guideRepository.existsByIdNotAndOrder(
                guideId,
                useCase.getOrder()
            )) {
                throw new GeneralException(GuideErrorCode.DUPLICATED_GUIDE_TRANSLATION_LANGUAGE);
            }

            // 2. 썸네일 업데이트
            Guide guide = readGuideService.findGuideByGuideId(guideId);
            uploadFileService.updateUploadFile(
                guide.getThumbnailImageId(),
                useCase.getThumbnailImage(),
                DirectoryConstants.GUIDE_THUMBNAIL_DIRECTORY
            );

            // 3. 업데이트
            guideRepository.updateGuide(useCase);
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }
}
