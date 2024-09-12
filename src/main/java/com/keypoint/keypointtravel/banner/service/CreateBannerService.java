package com.keypoint.keypointtravel.banner.service;

import com.keypoint.keypointtravel.banner.dto.useCase.SaveLocaleUseCase;
import com.keypoint.keypointtravel.banner.dto.useCase.SaveUseCase;
import com.keypoint.keypointtravel.banner.entity.Banner;
import com.keypoint.keypointtravel.banner.entity.BannerContent;
import com.keypoint.keypointtravel.banner.repository.banner.BannerContentRepository;
import com.keypoint.keypointtravel.banner.repository.banner.BannerRepository;
import com.keypoint.keypointtravel.global.enumType.error.BannerErrorCode;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
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
        // 삭제 여부
        boolean isDeleted = false;
        // 배너 생성
        Banner banner = useCase.toEntity(isDeleted);
        // 배너 내용 생성
        BannerContent bannerContent = useCase.toEntity(banner, isDeleted);
        // 배너 저장
        bannerRepository.save(banner);
        bannerContentRepository.save(bannerContent);
    }

    /**
     * Banner 이미 생성된 배너에 다른 언어로 생성하는 함수 (공통 배너 생성)
     *
     * @Param 배너 생성 정보 useCase
     */
    @Transactional
    public void saveBannerByOtherLanguage(SaveLocaleUseCase useCase) {
        // 1. 이미 bannerId에 해당하는 배너에 저장할 언어로 배너 내용이 있는지 확인
        if (bannerRepository.isExistBannerContentByLanguageCode(useCase.getBannerId(),
            useCase.getLanguageCode())) {
            throw new GeneralException(BannerErrorCode.EXISTS_BANNER_CONTENT);
        }
        // 2. 배너 영어 버전 조회
        BannerContent bannerContent = bannerContentRepository
            .findByBannerIdAndLanguageCode(useCase.getBannerId(), LanguageCode.EN)
            .orElseThrow(() -> new GeneralException(BannerErrorCode.NOT_EXISTED_BANNER));
        // 배너 내용 생성
        BannerContent newBannerContent = useCase.toEntity(bannerContent);
        // 배너 내용 저장
            bannerContentRepository.save(newBannerContent);
    }
}