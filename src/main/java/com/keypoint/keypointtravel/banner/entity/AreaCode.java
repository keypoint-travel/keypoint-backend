package com.keypoint.keypointtravel.banner.entity;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AreaCode implements BannerCode {
    SEOUL("1", "서울"),
    INCHEON("2", "인천"),
    DAEJEON("3", "대전"),
    DAEGU("4", "대구"),
    GWANGJU("5", "광주"),
    BUSAN("6", "부산"),
    ULSAN("7", "울산"),
    SEJONG("8", "세종"),
    GYEONGGI("31", "경기"),
    GANGWON("32", "강원"),
    CHUNGBUK("33", "충북"),
    CHUNGNAM("34", "충남"),
    JEONBUK("35", "경북"),
    JEONNAM("36", "경남"),
    GYEONGBUK("37", "전북"),
    GYEONGNAM("38", "전남"),
    JEJU("39", "제주");

    private final String code;
    private final String description;
}