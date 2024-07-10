package com.keypoint.keypointtravel.banner.controller;


import com.keypoint.keypointtravel.banner.dto.request.BannerListRequest;
import com.keypoint.keypointtravel.banner.dto.response.BannerListResponse;
import com.keypoint.keypointtravel.banner.entity.AreaCode;
import com.keypoint.keypointtravel.banner.entity.BannerCode;
import com.keypoint.keypointtravel.banner.entity.ContentType;
import com.keypoint.keypointtravel.banner.entity.LargeCategory;
import com.keypoint.keypointtravel.banner.entity.MiddleCategory;
import com.keypoint.keypointtravel.banner.entity.SmallCategory;
import com.keypoint.keypointtravel.banner.service.TourismApiService;
import com.keypoint.keypointtravel.banner.dto.useCase.tourListUseCase.TourismListUseCase;
import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.constants.TourismApiConstants;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/banners")
@RequiredArgsConstructor
public class CreateBannerController {

    private final TourismApiService tourismApiService;

    @GetMapping
    public APIResponseEntity<BannerListResponse> findBannerList(
        @ModelAttribute BannerListRequest bannerListRequest,
        @AuthenticationPrincipal CustomUserDetails userDetails) {

        //todo: 관리자 인증 로직 추가 예정

        TourismListUseCase useCase = tourismApiService.findTourismList(
            bannerListRequest.getPage(),
            TourismApiConstants.SERVICE_KEY,
            BannerCode.getConstant(AreaCode.class, bannerListRequest.getRegion()).getCode(),
            BannerCode.getConstant(ContentType.class, bannerListRequest.getTourType()).getCode(),
            BannerCode.getConstant(LargeCategory.class, bannerListRequest.getCat1()).getCode(),
            BannerCode.getConstant(MiddleCategory.class, bannerListRequest.getCat2()).getCode(),
            BannerCode.getConstant(SmallCategory.class, bannerListRequest.getCat3()).getCode()
        );

        return APIResponseEntity.<BannerListResponse>builder()
            .data(BannerListResponse.from(useCase.getResponse().getBody()))
            .build();
    }
}
