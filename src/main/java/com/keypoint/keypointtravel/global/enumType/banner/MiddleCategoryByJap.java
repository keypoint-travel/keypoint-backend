package com.keypoint.keypointtravel.global.enumType.banner;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MiddleCategoryByJap implements BannerCode {
    NATURAL_SITES("A0101", "自然観光地"),
    NATURAL_RESOURCES("A0102", "観光資源"),
    HISTORICAL_SITES("A0201", "歴史観光地"),
    RECREATIONAL_SITES("A0202", "休養観光地"),
    EXPERIENCE_PROGRAMS("A0203", "体験観光地"),
    INDUSTRIAL_SITES("A0204", "産業観光地"),
    ARCHITECTURAL_SIGHTS("A0205", "建築／オブジェ"),
    CULTURAL_FACILITIES("A0206", "文化施設"),
    FESTIVALS("A0207", "祭り"),
    EVENTS_PERFORMANCES("A0208", "公演／イベント"),
    INTRODUCTION("A0301", "レジャースポーツ紹介"),
    LEISURE_SPORTS_LAND("A0302", "陸上レジャースポーツ"),
    LEISURE_SPORTS_WATER("A0303", "水上レジャースポーツ"),
    LEISURE_SPORTS_SKY("A0304", "空中レジャースポーツ"),
    LEISURE_SPORTS_OTHERS("A0305", "複合レジャースポーツ"),
    ACCOMMODATIONS("B0201", "宿泊施設"),
    SHOPPING("A0401", "ショッピング"),
    RESTAURANTS("A0502", "飲食店"),
    TRANSPORTATION_FACILITIES("B0102", "交通施設");

    private final String code;
    private final String description;
}
