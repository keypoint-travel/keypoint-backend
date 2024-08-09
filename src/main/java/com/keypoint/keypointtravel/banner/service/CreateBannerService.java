package com.keypoint.keypointtravel.banner.service;

import com.keypoint.keypointtravel.banner.dto.useCase.SaveUseCase;
import com.keypoint.keypointtravel.banner.entity.Banner;
import com.keypoint.keypointtravel.banner.entity.BannerContent;
import com.keypoint.keypointtravel.banner.repository.banner.BannerContentRepository;
import com.keypoint.keypointtravel.banner.repository.banner.BannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateBannerService {

    private final BannerRepository bannerRepository;

    private final BannerContentRepository bannerContentRepository;

    /**
     * Banner 배너를 처음 생성하는 함수 (공통 배너 생성)
     *
     * @Param 배너 생성 정보 useCase
     */
    @Transactional
    public void saveBanner(SaveUseCase useCase) {
        //삭제 여부
        boolean isDeleted = false;
        //배너 생성
        Banner banner = useCase.toEntity(isDeleted);
        //배너 내용 생성
        BannerContent bannerContent = useCase.toEntity(banner, isDeleted);
        bannerRepository.save(banner);
        bannerContentRepository.save(bannerContent);
    }
}