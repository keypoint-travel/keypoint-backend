package com.keypoint.keypointtravel.banner.controller;


import com.keypoint.keypointtravel.banner.dto.response.ThumbnailListResponse;
import com.keypoint.keypointtravel.banner.service.AdvertisementBannerService;
import com.keypoint.keypointtravel.banner.service.FindBannerService;
import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FindBannerThumbnailController {

    private final FindBannerService findBannerService;

    private final AdvertisementBannerService advertisementBannerService;

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("/api/v1/banners/exposure")
    public APIResponseEntity<ThumbnailListResponse> findThumbnailList(
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        ThumbnailListResponse response = new ThumbnailListResponse();
        response.addCommonBanner(findBannerService.findThumbnailList(userDetails.getId()));
        response.addAdvertisementBanner(advertisementBannerService.findThumbnailList(userDetails.getId()));
        return APIResponseEntity.<ThumbnailListResponse>builder()
            .message("노출된 배너 목록 조회")
            .data(response)
            .build();
    }
}
