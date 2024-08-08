package com.keypoint.keypointtravel.banner.dto.response;

import com.keypoint.keypointtravel.banner.dto.useCase.AdvertisementThumbnailDto;
import com.keypoint.keypointtravel.banner.dto.useCase.CommonBannerThumbnailUseCase;
import com.keypoint.keypointtravel.global.enumType.banner.BannerType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ThumbnailListResponse {

    private List<BannerThumbnail> banners = new ArrayList<>();

    public void addCommonBanner(List<CommonBannerThumbnailUseCase> commonBanners) {
        for (CommonBannerThumbnailUseCase banner : commonBanners) {
            banners.add(new BannerThumbnail(
                banner.getContentId(),
                banner.getThumbnailImageUrl(),
                banner.getMainTitle(),
                banner.getSubTitle(),
                BannerType.COMMON_BANNER)
            );
        }
    }

    public void addAdvertisementBanner(List<AdvertisementThumbnailDto> advertisementBanners) {
        for (AdvertisementThumbnailDto banner : advertisementBanners) {
            banners.add(new BannerThumbnail(
                String.valueOf(banner.getBannerId()),
                banner.getThumbnailImageUrl(),
                banner.getMainTitle(),
                banner.getSubTitle(),
                BannerType.ADVERTISEMENT_BANNER)
            );
        }
    }
}
