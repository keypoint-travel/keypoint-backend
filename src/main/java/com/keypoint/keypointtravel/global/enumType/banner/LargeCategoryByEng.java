package com.keypoint.keypointtravel.global.enumType.banner;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LargeCategoryByEng implements BannerCode {
    NATURE("A01", "Nature"),
    CULTURE_ART_HISTORY("A02", "Culture/Art/History"),
    LEISURE_SPORTS("A03", "Leisure/Sports"),
    ACCOMMODATION("B02", "Accommodation"),
    SHOPPING("A04", "Shopping"),
    CUISINE("A05", "Cuisine"),
    TRANSPORTATION("B01", "Transportation");

    private final String code;
    private final String description;
}
