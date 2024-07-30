package com.keypoint.keypointtravel.banner.controller;


import com.keypoint.keypointtravel.banner.dto.response.ThumbnailListResponse;
import com.keypoint.keypointtravel.banner.service.AdvertisementBannerService;
import com.keypoint.keypointtravel.banner.service.FindBannerService;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FindBannerThumbnailController {

    private final FindBannerService findBannerService;

    private final AdvertisementBannerService advertisementBannerService;

    @GetMapping("/api/v1/banners/exposure")
    public APIResponseEntity<ThumbnailListResponse> findThumbnailList() {

        return APIResponseEntity.<ThumbnailListResponse>builder()
            .message("노출된 배너 목록 조회")
            .data(new ThumbnailListResponse(
                findBannerService.findThumbnailList(),
                advertisementBannerService.findThumbnailList()))
            .build();
    }
}
