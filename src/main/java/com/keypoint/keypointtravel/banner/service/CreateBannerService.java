package com.keypoint.keypointtravel.banner.service;

import com.keypoint.keypointtravel.banner.dto.useCase.SaveUseCase;
import com.keypoint.keypointtravel.banner.entity.Banner;
import com.keypoint.keypointtravel.banner.repository.BannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateBannerService {

    private final BannerRepository bannerRepository;

    /**
     * Banner 생성하는 함수 (공통 배너 생성)
     *
     * @param useCase
     */
    @Transactional
    public void saveBanner(SaveUseCase useCase) {

        //사용자에게 노출 여부
        boolean isExposed = true;

        //배너 생성
        Banner banner = new Banner(useCase.getContentId(), useCase.getAreaCode(),
            useCase.getCat1(), useCase.getCat2(), useCase.getCat3(), useCase.getContentType(),
            useCase.getThumbnailTitle(), useCase.getThumbnailImage(), isExposed);

        bannerRepository.save(banner);
    }
}