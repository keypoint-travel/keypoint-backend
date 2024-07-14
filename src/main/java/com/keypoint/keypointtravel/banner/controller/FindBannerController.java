package com.keypoint.keypointtravel.banner.controller;

import com.keypoint.keypointtravel.banner.dto.response.BannerListResponse;
import com.keypoint.keypointtravel.banner.service.FindBannerService;
import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/banners")
@RequiredArgsConstructor
public class FindBannerController {

    private final FindBannerService findBannerService;

    @GetMapping("management")
    public APIResponseEntity<BannerListResponse> findBannerList(@AuthenticationPrincipal CustomUserDetails userDetails) {

        //todo: 관리자 인증 로직 추가 예정

        return APIResponseEntity.<BannerListResponse>builder()
            .message("생성한 공통 배너 목록 조회")
            .data(BannerListResponse.from(findBannerService.findBannerList()))
            .build();
    }
}