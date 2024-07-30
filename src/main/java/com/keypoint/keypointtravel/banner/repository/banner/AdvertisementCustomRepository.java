package com.keypoint.keypointtravel.banner.repository.banner;

import com.keypoint.keypointtravel.banner.dto.dto.AdvertisementBannerDto;

import java.util.List;

public interface AdvertisementCustomRepository {

    List<AdvertisementBannerDto> findAdvertisementBanners();

    Long updateIsExposedById(Long bannerId);
}
