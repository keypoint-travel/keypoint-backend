package com.keypoint.keypointtravel.banner.service;

import com.keypoint.keypointtravel.banner.dto.useCase.EditBannerUseCase;
import com.keypoint.keypointtravel.banner.repository.banner.BannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EditBannerService {

    private final BannerRepository bannerRepository;

    /**
     * Banner 언어별 수정하는 함수
     *
     * @Param bannerId, language 및 수정 정보 useCase
     */
    @Transactional
    public void editBanner(EditBannerUseCase useCase){
        bannerRepository.updateBannerContentById(useCase);
    }
}
