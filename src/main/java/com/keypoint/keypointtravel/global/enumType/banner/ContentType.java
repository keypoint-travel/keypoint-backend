package com.keypoint.keypointtravel.global.enumType.banner;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ContentType implements BannerCode {
    TOURIST_ATTRACTIONS("12", "관광지"),
    CULTURAL_FACILITIES("14", "문화시설"),
    FESTIVALS_EVENTS_PERFORMANCES("15", "행사/공연/축제"),
    TOURIST_COURSE("25", "여행코스"),
    LEISURE_SPORTS("28", "레포츠"),
    ACCOMMODATION("32", "숙박"),
    SHOPPING("38", "쇼핑"),
    DINING("39", "음식점");

    private final String code;
    private final String description;
}