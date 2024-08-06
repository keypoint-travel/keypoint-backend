package com.keypoint.keypointtravel.banner.controller;


import com.keypoint.keypointtravel.banner.dto.request.BannerListRequest;
import com.keypoint.keypointtravel.banner.dto.request.BannerRequest;
import com.keypoint.keypointtravel.banner.dto.response.ContentListResponse;
import com.keypoint.keypointtravel.banner.dto.response.ImageListResponse;
import com.keypoint.keypointtravel.banner.dto.useCase.SaveUseCase;
import com.keypoint.keypointtravel.banner.dto.useCase.imageListUseCase.ImageListUseCase;
import com.keypoint.keypointtravel.banner.service.CreateBannerService;
import com.keypoint.keypointtravel.banner.service.TourismApiService;
import com.keypoint.keypointtravel.banner.dto.useCase.tourListUseCase.TourismListUseCase;
import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.global.enumType.banner.AreaCode;
import com.keypoint.keypointtravel.global.enumType.banner.BannerCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/banners")
@RequiredArgsConstructor
public class CreateBannerController {

    private final TourismApiService tourismApiService;

    private final CreateBannerService bannerService;

    @Value("${key.tourApi.key}")
    private String serviceKey;

    @GetMapping
    public APIResponseEntity<ContentListResponse> findContentList(
        @ModelAttribute BannerListRequest bannerListRequest,
        @AuthenticationPrincipal CustomUserDetails userDetails) {

        //todo: 관리자 인증 로직 추가 예정

        TourismListUseCase useCase = tourismApiService.findTourismList(
            bannerListRequest.getPage(),
            serviceKey,
            BannerCode.getConstant(AreaCode.class, bannerListRequest.getRegion()).getCode(),
            bannerListRequest.getTourType(),
            bannerListRequest.getCat1(),
            bannerListRequest.getCat2(),
            bannerListRequest.getCat3()
        );

        return APIResponseEntity.<ContentListResponse>builder()
            .message("생성할 배너 리스트 조회")
            .data(ContentListResponse.from(useCase.getResponse().getBody()))
            .build();
    }

    @GetMapping("/{contentId}/images")
    public APIResponseEntity<ImageListResponse> findContentImageList(
        @PathVariable("contentId") String contentId,
        @AuthenticationPrincipal CustomUserDetails userDetails) {

        //todo: 관리자 인증 로직 추가 예정

        ImageListUseCase useCase = tourismApiService.findImageList(contentId, serviceKey);

        return APIResponseEntity.<ImageListResponse>builder()
            .message("contentId에 해당하는 이미지 리스트 조회")
            .data(ImageListResponse.of(contentId, useCase.getResponse().getBody().getItems()))
            .build();
    }

    @PostMapping
    public ResponseEntity<Void> saveBanner(
        @RequestBody @Valid BannerRequest request,
        @AuthenticationPrincipal CustomUserDetails userDetails) {

        //todo: 관리자 인증 로직 추가 예정

        bannerService.saveBanner(SaveUseCase.from(request));

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}