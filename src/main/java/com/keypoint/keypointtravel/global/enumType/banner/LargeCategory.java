package com.keypoint.keypointtravel.global.enumType.banner;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LargeCategory implements BannerCode {
    NATURE("A01", "자연"),
    CULTURE_ART_HISTORY("A02", "인문(문화/예술/역사)"),
    LEISURE_SPORTS("A03", "레포츠"),
    SHOPPING("A04", "쇼핑"),
    CUISINE("A05", "음식"),
    ACCOMMODATION("B02", "숙박"),
    RECOMMENDED_COURSE("C01", "추천코스");

    private final String code;
    private final String description;
}