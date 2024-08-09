package com.keypoint.keypointtravel.global.enumType.banner;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AreaCode implements BannerCode {
    SEOUL("1", "서울", "Seoul","ソウル特別市"),
    INCHEON("2", "인천", "Incheon","仁川広域市"),
    DAEJEON("3", "대전", "Daejeon","大田広域市"),
    DAEGU("4", "대구", "Daegu","大邱広域市"),
    GWANGJU("5", "광주", "Gwangju","光州広域市"),
    BUSAN("6", "부산", "Busan","釜山広域市"),
    ULSAN("7", "울산", "Ulsan","蔚山広域市"),
    SEJONG("8", "세종", "Sejong","世宗特別自治市"),
    GYEONGGI("31", "경기", "Gyeonggi-do","京畿道"),
    GANGWON("32", "강원", "Gangwon-do","江原道"),
    CHUNGBUK("33", "충북", "Chungcheongbuk-do","忠清北道"),
    CHUNGNAM("34", "충남", "Chungcheongnam-do","忠清南道"),
    JEONBUK("35", "경북", "Gyeongsangbuk-do","慶尚北道"),
    JEONNAM("36", "경남", "Gyeongsangnam-do","慶尚南道"),
    GYEONGBUK("37", "전북", "Jeonbuk-do", "チョンブク特別自治道"),
    GYEONGNAM("38", "전남", "Jeollanam-do", "全羅南道"),
    JEJU("39", "제주", "Jeju-do", "済州特別自治道");

    private final String code;
    private final String description;
    private final String engDescription;
    private final String japDescription;
}