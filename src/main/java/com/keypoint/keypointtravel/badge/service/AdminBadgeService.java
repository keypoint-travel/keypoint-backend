package com.keypoint.keypointtravel.badge.service;

import com.keypoint.keypointtravel.badge.dto.useCase.CreateBadgeUseCase;
import com.keypoint.keypointtravel.badge.entity.Badge;
import com.keypoint.keypointtravel.badge.respository.BadgeRepository;
import com.keypoint.keypointtravel.global.constants.DirectoryConstants;
import com.keypoint.keypointtravel.global.enumType.error.BadgeErrorCode;
import com.keypoint.keypointtravel.global.enumType.error.CommonErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.uploadFile.service.UploadFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminBadgeService {

    private final BadgeRepository badgeRepository;
    private final UploadFileService uploadFileService;

    /**
     * 배지 등록 함수
     *
     * @param useCase
     */
    @Transactional
    public void addBadge(CreateBadgeUseCase useCase) {
        try {
            // 1. 유효성 검사
            if (badgeRepository.existsByName(useCase.getName())) {
                throw new GeneralException(BadgeErrorCode.DUPLICATED_BADGE_NAME);
            }
            if (badgeRepository.existsByOrder(useCase.getOrder())) {
                throw new GeneralException(CommonErrorCode.DUPLICATED_ORDER);
            }

            // 2. 배지 이미지 저장
            Long activeBadgeId = uploadFileService.saveUploadFile(
                useCase.getBadgeOnImage(), DirectoryConstants.BADGE_DIRECTORY
            );
            Long inactiveBadgeId = uploadFileService.saveUploadFile(
                useCase.getBadgeOffImage(), DirectoryConstants.BADGE_DIRECTORY
            );

            // 3. 배지 저장
            Badge badge = useCase.toEntity(activeBadgeId, inactiveBadgeId);
            badgeRepository.save(badge);
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }
}
