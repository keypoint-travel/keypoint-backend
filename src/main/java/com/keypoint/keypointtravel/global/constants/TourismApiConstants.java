package com.keypoint.keypointtravel.global.constants;

public class TourismApiConstants {

    public static final String COMMON_URI = "https://apis.data.go.kr/B551011";
    public static final String KOREAN = "KorService1";
    public static final String ENGLISH = "EngService1";
    public static final String JAPANESE = "JpnService1";
    public static final String COMMON_OPTION = "&MobileOS=ETC&MobileApp=keypoint&_type=json";
    public static final String FIND_TOURISM_LIST = "/areaBasedList1?arrange=O";
    public static final String FIND_TOURISM = "/detailCommon1?defaultYN=Y&firstImageYN=Y&areacodeYN=Y&catcodeYN=Y&addrinfoYN=Y&mapinfoYN=Y&overviewYN=Y";
    public static final String FIND_IMAGE_LIST = "/detailImage1?subImageYN=Y&numOfRows=20";
    public static final String FIND_AROUND = "/locationBasedList1?arrange=S&radius=20000&numOfRows=4&pageNo=1";

    // todo : 임시 설정
    public static final String DEFAULT_IMAGE = "https://back-dev.keypointtravel.com/images/banner-default.png";
}