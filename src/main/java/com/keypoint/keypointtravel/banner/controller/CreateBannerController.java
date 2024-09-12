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
import com.keypoint.keypointtravel.global.enumType.error.BannerErrorCode;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import jakarta.validation.Valid;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.keypoint.keypointtravel.global.constants.TourismApiConstants.*;

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
            findLanguageValue(bannerListRequest.getLanguageCode()),
            bannerListRequest.getPage(),
            bannerListRequest.getSize(),
            serviceKey,
            BannerCode.getConstant(AreaCode.class, bannerListRequest.getRegion()).getCode(),
            bannerListRequest.getTourType(),
            bannerListRequest.getCat1(),
            bannerListRequest.getCat2(),
            bannerListRequest.getCat3()
        );
        return APIResponseEntity.<ContentListResponse>builder()
            .message("생성할 배너 리스트 조회")
            .data(ContentListResponse.from(useCase.getResponse().getBody(),
                bannerListRequest.getLanguageCode()))
            .build();
    }

    @GetMapping("/{contentId}/images")
    public APIResponseEntity<ImageListResponse> findContentImageList(
        @PathVariable("contentId") String contentId,
        @RequestParam(value = "languageCode", required = false) LanguageCode languageCode,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        //todo: 관리자 인증 로직 추가 예정
        ImageListUseCase useCase = tourismApiService.findImageList(
            findLanguageValue(languageCode), contentId, serviceKey);
        return APIResponseEntity.<ImageListResponse>builder()
            .message("contentId에 해당하는 이미지 리스트 조회")
            .data(ImageListResponse.of(contentId, useCase.getResponse().getBody().getItems()))
            .build();
    }

    private String findLanguageValue(LanguageCode languageCode) {
        if (Objects.isNull(languageCode) || languageCode.equals(LanguageCode.EN)) {
            return ENGLISH;
        }
        if (languageCode.equals(LanguageCode.KO)) {
            return KOREAN;
        }
        if (languageCode.equals(LanguageCode.JA)) {
            return JAPANESE;
        }
        throw new GeneralException(BannerErrorCode.LANGUAGE_DATA_MISMATCH);
    }

    @PostMapping
    public ResponseEntity<Void> saveBanner(
        @RequestBody @Valid BannerRequest bannerRequest,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        //todo: 관리자 인증 로직 추가 예정
        bannerService.saveBanner(SaveUseCase.of(bannerRequest, bannerRequest.getLanguageCode()));

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 이미 생성된 배너에 다른 언어로 추가
    @PostMapping("/{bannerId}")
    public ResponseEntity<Void> saveBanner(
        @PathVariable(value = "bannerId", required = false) Long bannerId,
        @RequestBody @Valid BannerRequest BannerRequest,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        //todo: 관리자 인증 로직 추가 예정
        bannerService.saveBannerByOtherLanguage(
            SaveUseCase.of(BannerRequest, BannerRequest.getLanguageCode()), bannerId);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}