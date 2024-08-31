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
import com.keypoint.keypointtravel.global.exception.GeneralException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

import static com.keypoint.keypointtravel.global.constants.TourismApiConstants.*;

@RestController
@RequestMapping("/api/v1/banners")
@RequiredArgsConstructor
public class CreateBannerController {

    private final TourismApiService tourismApiService;

    private final CreateBannerService bannerService;

    private final LocaleResolver localeResolver;

    @Value("${key.tourApi.key}")
    private String serviceKey;

    @GetMapping
    public APIResponseEntity<ContentListResponse> findContentList(
        @ModelAttribute BannerListRequest bannerListRequest,
        @AuthenticationPrincipal CustomUserDetails userDetails,
        HttpServletRequest request) {
        //todo: 관리자 인증 로직 추가 예정
        Locale locale = localeResolver.resolveLocale(request);
        TourismListUseCase useCase = tourismApiService.findTourismList(
            findLanguageValue(locale.getLanguage()),
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
            .data(ContentListResponse.from(useCase.getResponse().getBody(), locale.getLanguage()))
            .build();
    }

    @GetMapping("/{contentId}/images")
    public APIResponseEntity<ImageListResponse> findContentImageList(
        @PathVariable("contentId") String contentId,
        @AuthenticationPrincipal CustomUserDetails userDetails,
        HttpServletRequest request) {
        //todo: 관리자 인증 로직 추가 예정
        Locale locale = localeResolver.resolveLocale(request);
        ImageListUseCase useCase = tourismApiService.findImageList(
            findLanguageValue(locale.getLanguage()), contentId, serviceKey);
        return APIResponseEntity.<ImageListResponse>builder()
            .message("contentId에 해당하는 이미지 리스트 조회")
            .data(ImageListResponse.of(contentId, useCase.getResponse().getBody().getItems()))
            .build();
    }

    private String findLanguageValue(String language) {
        if(language == null){
            throw new GeneralException(BannerErrorCode.LANGUAGE_DATA_MISMATCH);
        }
        if (language.equals("ko")) {
            return KOREAN;
        }
        if (language.equals("en")) {
            return ENGLISH;
        }
        if (language.equals("ja")) {
            return JAPANESE;
        }
        throw new GeneralException(BannerErrorCode.LANGUAGE_DATA_MISMATCH);
    }

    @PostMapping
    public ResponseEntity<Void> saveBanner(
        @RequestBody @Valid BannerRequest bannerRequest,
        @AuthenticationPrincipal CustomUserDetails userDetails,
        HttpServletRequest request) {
        //todo: 관리자 인증 로직 추가 예정
        Locale locale = localeResolver.resolveLocale(request);
        bannerService.saveBanner(SaveUseCase.of(bannerRequest, locale.getLanguage()));

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 이미 생성된 배너에 다른 언어로 추가
    @PostMapping("/{bannerId}")
    public ResponseEntity<Void> saveBanner(
        @PathVariable(value = "bannerId", required = false) Long bannerId,
        @RequestBody @Valid BannerRequest BannerRequest,
        @AuthenticationPrincipal CustomUserDetails userDetails,
        HttpServletRequest request) {
        //todo: 관리자 인증 로직 추가 예정
        Locale locale = localeResolver.resolveLocale(request);
        bannerService.saveBannerByOtherLanguage(SaveUseCase.of(BannerRequest, locale.getLanguage()), bannerId);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}