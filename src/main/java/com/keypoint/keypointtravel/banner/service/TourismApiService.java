package com.keypoint.keypointtravel.banner.service;

import com.keypoint.keypointtravel.banner.dto.useCase.imageListUseCase.ImageListUseCase;
import com.keypoint.keypointtravel.banner.dto.useCase.tourListUseCase.TourismListUseCase;
import com.keypoint.keypointtravel.banner.dto.useCase.tourUseCase.TourismUseCase;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import static com.keypoint.keypointtravel.global.constants.TourismApiConstants.*;

@FeignClient(name = "tourismApi", url = COMMON_URI, fallback = TourismApiServiceFallback.class)
public interface TourismApiService {

    /**
     * 한국관광공사 API를 이용하여 관광지 리스트를 조회하는 함수
     *
     * @Param 특정 관광지 리스트를 조회하기 위한 분류 값
     *
     * @Return 조회한 관광지 목록
     */
    @GetMapping("/{language}" + FIND_TOURISM_LIST + COMMON_OPTION)
    TourismListUseCase findTourismList(
        @PathVariable("language") String language,
        @RequestParam("pageNo") int pageNo,
        @RequestParam("numOfRows") int numOfRows,
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
     *
     * @Return 조회한 관광지 정보
     */
    @GetMapping("/{language}" + FIND_TOURISM + COMMON_OPTION)
    TourismUseCase findTourism(
        @PathVariable("language") String language,
        @RequestParam("contentId") String contentId,
        @RequestParam("serviceKey") String serviceKey);

    /**
     * 한국관광공사 API를 이용하여 특정 관광지의 이미지 리스트를 조회하는 함수
     *
     * @Param contentId, serviceKey
     *
     * @Return 조회한 관광지 이미지 목록
     */
    @GetMapping("/{language}" + FIND_IMAGE_LIST + COMMON_OPTION)
    ImageListUseCase findImageList(
        @PathVariable("language") String language,
        @RequestParam("contentId") String contentId,
        @RequestParam("serviceKey") String serviceKey);

    /**
     * 한국관광공사 API를 이용하여 특정 위치 주변 관광지 리스트를 조회하는 함수
     *
     * @Param contentId, serviceKey
     *
     * @Return 조회한 관광지 목록
     */
    @GetMapping("/{language}" + FIND_AROUND + COMMON_OPTION)
    TourismListUseCase findAround(
        @PathVariable("language") String language,
        @RequestParam("mapX") Double mapX,
        @RequestParam("mapY") Double mapY,
        @RequestParam("serviceKey") String serviceKey);
}