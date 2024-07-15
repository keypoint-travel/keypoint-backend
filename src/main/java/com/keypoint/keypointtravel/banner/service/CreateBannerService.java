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
     * @Param useCase
     */
    @Transactional
    public void saveBanner(SaveUseCase useCase) {

        //사용자에게 노출 여부
        boolean isExposed = true;

        //배너 생성
        Banner banner = Banner.builder()
            .id(useCase.getContentId())
            .title(useCase.getTitle())
            .areaCode(useCase.getAreaCode())
            .cat1(useCase.getCat1())
            .cat2(useCase.getCat2())
            .cat3(useCase.getCat3())
            .contentType(useCase.getContentType())
            .thumbnailTitle(useCase.getThumbnailTitle())
            .thumbnailImage(useCase.getThumbnailImage())
            .address1(useCase.getAddress1())
            .address2(useCase.getAddress2())
            .latitude(useCase.getLatitude())
            .longitude(useCase.getLongitude())
            .isExposed(isExposed)
            .build();

        bannerRepository.save(banner);
    }
}