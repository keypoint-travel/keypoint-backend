package com.keypoint.keypointtravel.banner.controller;

import com.keypoint.keypointtravel.banner.dto.response.BannerListResponse;
import com.keypoint.keypointtravel.banner.dto.response.commonBanner.CommonBannerResponse;
import com.keypoint.keypointtravel.banner.dto.response.ThumbnailListResponse;
import com.keypoint.keypointtravel.banner.dto.useCase.BannerUseCase;
import com.keypoint.keypointtravel.banner.dto.useCase.CommonTourismUseCase;
import com.keypoint.keypointtravel.banner.dto.useCase.tourListUseCase.TourismListUseCase;
import com.keypoint.keypointtravel.banner.service.FindBannerService;
import com.keypoint.keypointtravel.banner.service.TourismApiService;
import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/banners")
@RequiredArgsConstructor
public class FindBannerController {

    private final FindBannerService findBannerService;

    private final TourismApiService tourismApiService;

    @Value("${key.tourApi.key}")
    private String serviceKey;

    @GetMapping("management")
    public APIResponseEntity<BannerListResponse> findBannerList(@AuthenticationPrincipal CustomUserDetails userDetails) {

        //todo: 관리자 인증 로직 추가 예정

        return APIResponseEntity.<BannerListResponse>builder()
            .message("생성한 공통 배너 목록 조회")
            .data(new BannerListResponse(findBannerService.findBannerList()))
            .build();
    }

    @GetMapping("exposure")
    public APIResponseEntity<ThumbnailListResponse> findThumbnailList() {

        return APIResponseEntity.<ThumbnailListResponse>builder()
            .message("노출된 배너 목록 조회")
            .data(new ThumbnailListResponse(findBannerService.findThumbnailList()))
            .build();
    }

    @GetMapping("/{bannerId}/common")
    public APIResponseEntity<CommonBannerResponse> findCommonBanner(
        @PathVariable("bannerId") Long bannerId,
        @AuthenticationPrincipal CustomUserDetails userDetails) {

        CommonTourismUseCase details = findBannerService.findCommonBanner(new BannerUseCase(bannerId, userDetails));
        TourismListUseCase arounds = tourismApiService.findArounds(
            details.getCommonTourismDto().getLongitude(), details.getCommonTourismDto().getLatitude(), serviceKey);

        return APIResponseEntity.<CommonBannerResponse>builder()
            .message("공통 배너 상세 조회 ")
            .data(CommonBannerResponse.of(details, arounds.getResponse().getBody().getItems(), userDetails))
            .build();
    }
}