package com.keypoint.keypointtravel.banner.service;

import com.keypoint.keypointtravel.banner.dto.useCase.tourListUseCase.TourismListUseCase;
import com.keypoint.keypointtravel.banner.dto.useCase.tourUseCase.TourismUseCase;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "koreanTourismApi", url = "https://apis.data.go.kr/B551011/KorService1")
public interface TourismApiService {

    @GetMapping("/areaBasedList1?MobileOS=ETC&MobileApp=keypoint&arrange=O&_type=json")
    TourismListUseCase findTourismList(
        @RequestParam("pageNo") int pageNo,
        @RequestParam("serviceKey") String serviceKey,
        @RequestParam("areaCode") String areaCode,
        @RequestParam("contentTypeId") String contentTypeId,
        @RequestParam("cat1") String cat1,
        @RequestParam("cat2") String cat2,
        @RequestParam("cat3") String cat3);

    @GetMapping("/detailCommon1?MobileOS=ETC&MobileApp=keypoint&_type=json&defaultYN=Y&firstImageYN=Y&areacodeYN=Y&catcodeYN=Y&addrinfoYN=Y&mapinfoYN=Y&overviewYN=Y")
    TourismUseCase findTourism(
        @RequestParam("contentId") String contentId,
        @RequestParam("serviceKey") String serviceKey);
}