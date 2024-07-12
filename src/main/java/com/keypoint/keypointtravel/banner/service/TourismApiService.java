package com.keypoint.keypointtravel.banner.service;

import static com.keypoint.keypointtravel.global.constants.TourismApiConstants.COMMON_OPTION;
import static com.keypoint.keypointtravel.global.constants.TourismApiConstants.COMMON_URI;
import static com.keypoint.keypointtravel.global.constants.TourismApiConstants.FIND_IMAGE_LIST;
import static com.keypoint.keypointtravel.global.constants.TourismApiConstants.FIND_TOURISM;
import static com.keypoint.keypointtravel.global.constants.TourismApiConstants.FIND_TOURISM_LIST;

import com.keypoint.keypointtravel.banner.dto.useCase.imageListUseCase.ImageListUseCase;
import com.keypoint.keypointtravel.banner.dto.useCase.tourListUseCase.TourismListUseCase;
import com.keypoint.keypointtravel.banner.dto.useCase.tourUseCase.TourismUseCase;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "koreanTourismApi", url = COMMON_URI)
public interface TourismApiService {

    @GetMapping(FIND_TOURISM_LIST + COMMON_OPTION)
    TourismListUseCase findTourismList(
        @RequestParam("pageNo") int pageNo,
        @RequestParam("serviceKey") String serviceKey,
        @RequestParam("areaCode") String areaCode,
        @RequestParam("contentTypeId") String contentTypeId,
        @RequestParam("cat1") String cat1,
        @RequestParam("cat2") String cat2,
        @RequestParam("cat3") String cat3);

    @GetMapping(FIND_TOURISM + COMMON_OPTION)
    TourismUseCase findTourism(
        @RequestParam("contentId") String contentId,
        @RequestParam("serviceKey") String serviceKey);

    @GetMapping(FIND_IMAGE_LIST + COMMON_OPTION)
    ImageListUseCase findImageList(
        @RequestParam("contentId") String contentId,
        @RequestParam("serviceKey") String serviceKey);
}