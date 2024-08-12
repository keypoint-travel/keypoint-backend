package com.keypoint.keypointtravel.global.enumType.banner;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ContentTypeByEng implements BannerCode {
    TOURIST_ATTRACTIONS("76", "Tourist Attractions"),
    CULTURAL_FACILITIES("78", "Cultural Facilities"),
    FESTIVALS_EVENTS_PERFORMANCES("85", "Festivals/Events/Performances"),
    LEISURE_SPORTS("75", "Leisure/Sports"),
    ACCOMMODATION("80", "Accommodation"),
    SHOPPING("79", "Shopping"),
    DINING("82", "Dining"),
    TRANSPORTATION("77", "Transportation");

    private final String code;
    private final String description;
}
