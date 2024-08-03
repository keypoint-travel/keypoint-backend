package com.keypoint.keypointtravel.banner.dto.response;

import com.keypoint.keypointtravel.banner.dto.useCase.AdvertisementThumbnailDto;
import com.keypoint.keypointtravel.banner.dto.useCase.CommonBannerThumbnailUseCase;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ThumbnailListResponse {

    List<CommonBannerThumbnailUseCase> commonBanners;
    List<AdvertisementThumbnailDto> advertisementBanners;
}
