package com.keypoint.keypointtravel.banner.dto.response;

import com.keypoint.keypointtravel.banner.dto.dto.AdvertisementDetailDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdvertisementBannerResponse {

    private Long bannerId;
    private String mainTitle;
    private String subTitle;
    private String content;
    private String detailImageUrl;

    public static AdvertisementBannerResponse from(AdvertisementDetailDto dto) {
        return new AdvertisementBannerResponse(dto.getBannerId(), dto.getMainTitle(), dto.getSubTitle(), dto.getContent(), dto.getDetailImageUrl());
    }
}
