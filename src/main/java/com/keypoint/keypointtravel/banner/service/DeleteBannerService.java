package com.keypoint.keypointtravel.banner.service;

import com.keypoint.keypointtravel.banner.dto.useCase.DeleteUseCase;
import com.keypoint.keypointtravel.banner.repository.banner.BannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteBannerService {

    private final BannerRepository bannerRepository;

    /**
     * Banner 삭제하는 함수(isExposed를 false로) (공통 배너 삭제)
     *
     * @Param useCase
     */
    @Transactional
    public void deleteBanner(DeleteUseCase deleteUseCase) {
        bannerRepository.updateIsExposedById(deleteUseCase.bannerId());
    }
}