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
     * @Param bannerId를 담은 useCase
     */
    @Transactional
    public void deleteBanner(DeleteUseCase deleteUseCase) {
        // 언어 코드가 없을 경우, 해당 배너 및 모든 언어 코드에 해당하는 배너 내용 삭제
        if (deleteUseCase.getLanguageCode() == null) {
            bannerRepository.updateIsDeletedById(deleteUseCase.getBannerId());
            bannerRepository.updateContentIsDeletedById(deleteUseCase.getBannerId(), deleteUseCase.getLanguageCode());
            return;
        }
        // 언어 코드가 있을 경우, 해당 언어 코드에 해당하는 배너 내용 삭제
        bannerRepository.updateContentIsDeletedById(deleteUseCase.getBannerId(), deleteUseCase.getLanguageCode());
        // 해당 배너의 모든 언어 코드에 해당하는 배너 내용이 없을 경우, 배너 삭제
        if (!bannerRepository.existsBannerContentByBannerId(deleteUseCase.getBannerId())) {
            bannerRepository.updateIsDeletedById(deleteUseCase.getBannerId());
        }
    }
}