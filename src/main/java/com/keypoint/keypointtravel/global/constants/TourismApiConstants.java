package com.keypoint.keypointtravel.global.constants;

public class TourismApiConstants {

    public static final String COMMON_URI = "https://apis.data.go.kr/B551011/KorService1";
    public static final String COMMON_OPTION = "&MobileOS=ETC&MobileApp=keypoint&_type=json";
    public static final String FIND_TOURISM_LIST = "/areaBasedList1?arrange=O";
    public static final String FIND_TOURISM = "/detailCommon1?defaultYN=Y&firstImageYN=Y&areacodeYN=Y&catcodeYN=Y&addrinfoYN=Y&mapinfoYN=Y&overviewYN=Y";
    public static final String FIND_IMAGE_LIST = "/detailImage1?subImageYN=Y&numOfRows=20";
    public static final String FIND_AROUNDS = "/locationBasedList1?arrange=O&radius=20000&numOfRows=5&pageNo=1";
}