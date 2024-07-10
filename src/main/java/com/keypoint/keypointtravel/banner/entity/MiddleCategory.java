package com.keypoint.keypointtravel.banner.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MiddleCategory {
    NATURAL_TOURIST_SPOT("A0101", "자연관광지"),
    TOURIST_RESOURCE("A0102", "관광자원"),
    HISTORICAL_TOURIST_SPOT("A0201", "역사관광지"),
    RECREATIONAL_TOURIST_SPOT("A0202", "휴양관광지"),
    EXPERIENTIAL_TOURIST_SPOT("A0203", "체험관광지"),
    INDUSTRIAL_TOURIST_SPOT("A0204", "산업관광지"),
    ARCHITECTURE("A0205", "건축/조형물"),
    CULTURAL_FACILITY("A0206", "문화시설"),
    FESTIVAL("A0207", "축제"),
    PERFORMANCE_EVENT("A0208", "공연/행사"),
    FAMILY_COURSE("C0112", "가족코스"),
    SOLO_COURSE("C0113", "나홀로코스"),
    HEALING_COURSE("C0114", "힐링코스"),
    WALKING_COURSE("C0115", "도보코스"),
    CAMPING_COURSE("C0116", "캠핑코스"),
    TASTE_COURSE("C0117", "맛코스"),
    LEISURE_SPORTS_INTRODUCTION("A0301", "레포츠소개"),
    LAND_LEISURE_SPORTS("A0302", "육상 레포츠"),
    WATER_LEISURE_SPORTS("A0303", "수상 레포츠"),
    AIR_LEISURE_SPORTS("A0304", "항공 레포츠"),
    COMPLEX_LEISURE_SPORTS("A0305", "복합 레포츠"),
    ACCOMMODATION_FACILITY("B0201", "숙박시설"),
    SHOPPING("A0401", "쇼핑"),
    RESTAURANT("A0502", "음식점");

    private final String code;
    private final String description;

    public static MiddleCategory getMiddleCategory(String description) {
        for (MiddleCategory middleCategory : MiddleCategory.values()) {
            if (middleCategory.getDescription().equals(description)) {
                return middleCategory;
            }
        }
        return null;
    }

    public static String getDescription(String code) {
        for (MiddleCategory middleCategory : MiddleCategory.values()) {
            if (middleCategory.getCode().equals(code)) {
                return middleCategory.getDescription();
            }
        }
        return null;
    }
}
