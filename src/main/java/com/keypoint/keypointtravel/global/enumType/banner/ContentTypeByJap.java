package com.keypoint.keypointtravel.global.enumType.banner;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ContentTypeByJap implements BannerCode {
    TOURIST_ATTRACTIONS("76", "観光地"),
    CULTURAL_FACILITIES("78", "文化施設"),
    FESTIVALS_EVENTS_PERFORMANCES("85", "祭り／公演 / イベント"),
    LEISURE_SPORTS("75", "レジャースポーツ"),
    ACCOMMODATION("80", "宿泊"),
    SHOPPING("79", "ショッピング"),
    DINING("82", "グルメ"),
    TRANSPORTATION("77", "交通");

    private final String code;
    private final String description;
}
