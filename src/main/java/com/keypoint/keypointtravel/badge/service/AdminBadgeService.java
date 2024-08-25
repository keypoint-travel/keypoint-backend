package com.keypoint.keypointtravel.badge.service;

import com.keypoint.keypointtravel.badge.dto.request.BadgeIdRequest;
import com.keypoint.keypointtravel.badge.dto.response.BadgeInAdminResponse;
import com.keypoint.keypointtravel.badge.dto.useCase.CreateBadgeUseCase;
import com.keypoint.keypointtravel.badge.dto.useCase.DeleteBadgeUseCase;
import com.keypoint.keypointtravel.badge.dto.useCase.UpdateBadgeUseCase;
import com.keypoint.keypointtravel.badge.entity.Badge;
import com.keypoint.keypointtravel.badge.respository.BadgeRepository;
import com.keypoint.keypointtravel.global.constants.DirectoryConstants;
import com.keypoint.keypointtravel.global.dto.useCase.PageUseCase;
import com.keypoint.keypointtravel.global.enumType.error.BadgeErrorCode;
import com.keypoint.keypointtravel.global.enumType.error.CommonErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.uploadFile.service.UploadFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
            if (badgeRepository.existsByNameAndIsDeletedFalse(useCase.getName())) {
                throw new GeneralException(BadgeErrorCode.DUPLICATED_BADGE_NAME);
            }
            if (badgeRepository.existsByOrderAndIsDeletedFalse(useCase.getOrder())) {
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

    /**
     * 배지 수정 함수
     *
     * @param useCase
     */
    @Transactional
    public void updateBadge(UpdateBadgeUseCase useCase) {
        try {
            Long badgeId = useCase.getBadgeId();
            Badge badge = findBadgeByBadgeId(badgeId);

            // 1. 유효성 검사
            if (badgeRepository.existsByIdNotAndOrderAndIsDeletedFalse(
                badgeId,
                useCase.getOrder()
            )) {
                throw new GeneralException(CommonErrorCode.DUPLICATED_ORDER);
            }
            if (!useCase.getName().equals(badge.getName()) &&
                badgeRepository.existsByNameAndIsDeletedFalse(useCase.getName())
            ) {
                throw new GeneralException(BadgeErrorCode.DUPLICATED_BADGE_NAME);
            }

            // 2. 이미지 업데이트

            uploadFileService.updateUploadFile(
                badge.getActiveImageId(),
                useCase.getBadgeOnImage(),
                DirectoryConstants.BADGE_DIRECTORY
            );
            uploadFileService.updateUploadFile(
                badge.getInactiveImageId(),
                useCase.getBadgeOffImage(),
                DirectoryConstants.BADGE_DIRECTORY
            );

            // 3. 데이터 업데이트
            badgeRepository.updateBadge(useCase);
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }

    private Badge findBadgeByBadgeId(Long badgeId) {
        return badgeRepository.findByIdAndIsDeletedFalse(badgeId).orElseThrow(
            () -> new GeneralException(BadgeErrorCode.NOT_EXISTED_BADGE)
        );
    }

    /**
     * 배지 삭제 성공
     *
     * @param useCase
     */
    public void deleteBadge(DeleteBadgeUseCase useCase) {
        try {
            badgeRepository.deleteGuides(useCase);
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }

    public BadgeInAdminResponse findBadgeById(BadgeIdRequest useCase) {
        try {
            return badgeRepository.findBadgeById(useCase);
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }

    public Page<BadgeInAdminResponse> findBadges(PageUseCase useCase) {
        try {
            return badgeRepository.findBadges(useCase);
        } catch (Exception ex) {
            throw new GeneralException(ex);
        }
    }
}
