package com.keypoint.keypointtravel.banner.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AdvertisementBannerListResponse {

    List<AdvertisementBannerUseCase> banners;
}
