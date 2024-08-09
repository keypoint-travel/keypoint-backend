package com.keypoint.keypointtravel.global.enumType.banner;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LargeCategoryByJap implements BannerCode {
    NATURE("A01", "自然"),
    CULTURE_ART_HISTORY("A02", "人文（文化／芸術／歴史）"),
    LEISURE_SPORTS("A03", "レジャースポーツ"),
    ACCOMMODATION("B02", "宿泊"),
    SHOPPING("A04", "ショッピング"),
    CUISINE("A05", "グルメ"),
    TRANSPORTATION("B01", "交通");

    private final String code;
    private final String description;
}
