package com.keypoint.keypointtravel.global.enumType.banner;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BannerType {

    COMMON_BANNER("commonBanner", "공통 배너"),
    ADVERTISEMENT_BANNER("advertisementBanner", "광고 배너");

    private final String code;
    private final String description;
}
