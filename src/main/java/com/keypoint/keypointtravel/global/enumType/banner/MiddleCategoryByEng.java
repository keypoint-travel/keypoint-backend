package com.keypoint.keypointtravel.global.enumType.banner;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MiddleCategoryByEng implements BannerCode {
    NATURAL_SITES("A0101", "Natural Sites"),
    NATURAL_RESOURCES("A0102", "Natural Resources"),
    HISTORICAL_SITES("A0201", "Historical Sites"),
    RECREATIONAL_SITES("A0202", "Recreational Sites"),
    EXPERIENCE_PROGRAMS("A0203", "Experience Programs"),
    INDUSTRIAL_SITES("A0204", "Industrial Sites"),
    ARCHITECTURAL_SIGHTS("A0205", "Architectural Sights"),
    CULTURAL_FACILITIES("A0206", "Cultural Facilities"),
    FESTIVALS("A0207", "Festivals"),
    EVENTS_PERFORMANCES("A0208", "Events/Performances"),
    INTRODUCTION("A0301", "Introduction"),
    LEISURE_SPORTS_LAND("A0302", "Leisure/Sports (Land)"),
    LEISURE_SPORTS_WATER("A0303", "Leisure/Sports (Water)"),
    LEISURE_SPORTS_SKY("A0304", "Leisure/Sports (Sky)"),
    LEISURE_SPORTS_OTHERS("A0305", "Leisure/Sports (Others)"),
    ACCOMMODATIONS("B0201", "Accommodations"),
    SHOPPING("A0401", "Shopping"),
    RESTAURANTS("A0502", "Restaurants"),
    TRANSPORTATION_FACILITIES("B0102", "Transportation Facilities");

    private final String code;
    private final String description;
}
