package com.keypoint.keypointtravel.banner.service;

import com.keypoint.keypointtravel.banner.dto.useCase.imageListUseCase.ImageListUseCase;
import com.keypoint.keypointtravel.banner.dto.useCase.tourListUseCase.TourismListUseCase;
import com.keypoint.keypointtravel.banner.dto.useCase.tourUseCase.TourismUseCase;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.keypoint.keypointtravel.global.constants.TourismApiConstants.*;

@FeignClient(name = "koreanTourismApi", url = COMMON_URI, fallback = TourismApiServiceFallback.class)
public interface TourismApiService {

    /**
     * 한국관광공사 API를 이용하여 관광지 리스트를 조회하는 함수
     *
     * @Param
     * @Return
     */
    @GetMapping(FIND_TOURISM_LIST + COMMON_OPTION)
    TourismListUseCase findTourismList(
        @RequestParam("pageNo") int pageNo,
        @RequestParam("serviceKey") String serviceKey,
        @RequestParam("areaCode") String areaCode,
        @RequestParam("contentTypeId") String contentTypeId,
        @RequestParam("cat1") String cat1,
        @RequestParam("cat2") String cat2,
        @RequestParam("cat3") String cat3);

    /**
     * 한국관광공사 API를 이용하여 특정 관광지를 조회하는 함수
     *
     * @Param contentId, serviceKey
     * @Return
     */
    @GetMapping(FIND_TOURISM + COMMON_OPTION)
    TourismUseCase findTourism(
        @RequestParam("contentId") String contentId,
        @RequestParam("serviceKey") String serviceKey);

    /**
     * 한국관광공사 API를 이용하여 특정 관광지의 이미지 리스트를 조회하는 함수
     *
     * @Param contentId, serviceKey
     * @Return
     */
    @GetMapping(FIND_IMAGE_LIST + COMMON_OPTION)
    ImageListUseCase findImageList(
        @RequestParam("contentId") String contentId,
        @RequestParam("serviceKey") String serviceKey);

    @GetMapping(FIND_AROUNDS + COMMON_OPTION)
    TourismListUseCase findArounds(
        @RequestParam("mapX") Double mapX,
        @RequestParam("mapY") Double mapY,
        @RequestParam("serviceKey") String serviceKey);
}