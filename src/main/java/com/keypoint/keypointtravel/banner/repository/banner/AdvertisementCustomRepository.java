package com.keypoint.keypointtravel.banner.repository.banner;

import com.keypoint.keypointtravel.banner.dto.dto.AdvertisementBannerDto;
import com.keypoint.keypointtravel.banner.dto.dto.AdvertisementDetailDto;
import com.keypoint.keypointtravel.banner.dto.useCase.AdvertisementThumbnailDto;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;

import java.util.List;

public interface AdvertisementCustomRepository {

    List<AdvertisementBannerDto> findAdvertisementBanners();

    void updateIsDeletedById(Long bannerId);

    void updateContentIsDeletedById(Long bannerId, LanguageCode languageCode);

    boolean existsBannerContentByBannerId(Long bannerId);

    AdvertisementDetailDto findAdvertisementBannerById(Long bannerId, LanguageCode languageCode);

    List<AdvertisementThumbnailDto> findAdvertisementThumbnailList(Long memberId);

    boolean isExistBannerContentByLanguageCode(Long bannerId, LanguageCode languageCode);
}
