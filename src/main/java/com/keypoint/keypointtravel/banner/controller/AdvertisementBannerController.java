package com.keypoint.keypointtravel.banner.controller;

import com.keypoint.keypointtravel.banner.dto.dto.AdvertisementDetailDto;
import com.keypoint.keypointtravel.banner.dto.request.AdvertisementRequest;
import com.keypoint.keypointtravel.banner.dto.response.*;
import com.keypoint.keypointtravel.banner.dto.useCase.*;
import com.keypoint.keypointtravel.banner.dto.useCase.advertisement.AdvertisementUseCase;
import com.keypoint.keypointtravel.banner.service.AdvertisementBannerService;
import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.global.enumType.error.BannerErrorCode;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.member.entity.Member;
import com.keypoint.keypointtravel.member.service.ReadMemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.LocaleResolver;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/v1/banners/advertisement")
@RequiredArgsConstructor
public class AdvertisementBannerController {

    private final AdvertisementBannerService advertisementBannerService;

    private final ReadMemberService readMemberService;

    private final LocaleResolver localeResolver;

    @GetMapping("/image")
    public APIResponseEntity<ImageUrlResponse> transformToUrl(
        @RequestPart(value = "image") MultipartFile image,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        //todo: 관리자 인증 로직 추가 예정
        ImageUrlResponse response = advertisementBannerService.uploadImage(new ImageUseCase(image));
        return APIResponseEntity.<ImageUrlResponse>builder()
            .message("이미지 파일 url 변환 성공")
            .data(response)
            .build();
    }

    @PostMapping
    public ResponseEntity<Void> saveAdvertisementBanner(
        @RequestPart(value = "thumbnailImage", required = false) MultipartFile thumbnailImage,
        @RequestPart(value = "detailImage", required = false) MultipartFile detailImage,
        @RequestPart(value = "detail") @Valid AdvertisementRequest bannerRequest,
        @AuthenticationPrincipal CustomUserDetails userDetails,
        HttpServletRequest request) {
        //todo: 관리자 인증 로직 추가 예정
        Locale locale = localeResolver.resolveLocale(request);
        AdvertisementUseCase useCase = new AdvertisementUseCase(
            thumbnailImage, detailImage, findLanguageValue(locale.getLanguage()), bannerRequest.getMainTitle(),
            bannerRequest.getSubTitle(), bannerRequest.getContent());
        advertisementBannerService.saveAdvertisementBanner(useCase);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 이미 생성된 배너에 다른 언어로 추가
    @PostMapping("/{bannerId}")
    public ResponseEntity<Void> saveAdvertisementBanner(
        @PathVariable(value = "bannerId", required = false) Long bannerId,
        @RequestBody @Valid AdvertisementRequest bannerRequest,
        @AuthenticationPrincipal CustomUserDetails userDetails,
        HttpServletRequest request) {
        //todo: 관리자 인증 로직 추가 예정
        Locale locale = localeResolver.resolveLocale(request);
        PlusAdvertisementUseCase useCase = new PlusAdvertisementUseCase(bannerId, bannerRequest.getMainTitle(),
            bannerRequest.getSubTitle(), bannerRequest.getContent(), findLanguageValue(locale.getLanguage()));
        advertisementBannerService.saveBannerByOtherLanguage(useCase);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public APIResponseEntity<AdvertisementBannerListResponse> findAdvertisementBannerList(
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        //todo: 관리자 인증 로직 추가 예정
        List<AdvertisementBannerUseCase> useCaseList = advertisementBannerService.findAdvertisementBanners();
        AdvertisementBannerListResponse response = new AdvertisementBannerListResponse(useCaseList);
        return APIResponseEntity.<AdvertisementBannerListResponse>builder()
            .message("광고 배너 목록 조회 성공")
            .data(response)
            .build();
    }

    // 전체 언어 삭제
    @DeleteMapping("/{bannerId}")
    public ResponseEntity<Void> deleteBanners(
        @PathVariable("bannerId") Long bannerId,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        //todo: 관리자 인증 로직 추가 예정
        advertisementBannerService.deleteBanner(new DeleteUseCase(bannerId, null));
        return ResponseEntity.noContent().build();
    }

    // 특정 언어 삭제
    @DeleteMapping("/{bannerId}/locale")
    public ResponseEntity<Void> deleteBanner(
        @PathVariable("bannerId") Long bannerId,
        @AuthenticationPrincipal CustomUserDetails userDetails,
        HttpServletRequest request) {
        //todo: 관리자 인증 로직 추가 예정
        Locale locale = localeResolver.resolveLocale(request);
        advertisementBannerService.deleteBanner(new DeleteUseCase(bannerId, locale.getLanguage()));
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("/{bannerId}")
    public APIResponseEntity<AdvertisementBannerResponse> findAdvertisementBanner(
        @PathVariable("bannerId") Long bannerId,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        // 회원의 등록 언어를 가져옴.
        Member member = readMemberService.findMemberById(userDetails.getId());
        LanguageCode languageCode = member.getMemberDetail().getLanguage();
        // DB에 저장된 광고 배너 조회
        AdvertisementDetailDto dto = advertisementBannerService.findAdvertisementBanner(
            new FindAdvertisementUseCase(languageCode, bannerId));
        AdvertisementBannerResponse response = AdvertisementBannerResponse.from(dto);

        return APIResponseEntity.<AdvertisementBannerResponse>builder()
            .message("광고 배너 상세 조회")
            .data(response)
            .build();
    }

    @PatchMapping("/{bannerId}")
    public ResponseEntity<Void> editAdvertisementBanner(
        @PathVariable("bannerId") Long bannerId,
        @RequestPart(value = "thumbnailImage", required = false) MultipartFile thumbnailImage,
        @RequestPart(value = "detailImage", required = false) MultipartFile detailImage,
        @RequestPart(value = "detail") @Valid AdvertisementRequest bannerRequest,
        @AuthenticationPrincipal CustomUserDetails userDetails,
        HttpServletRequest request) {
        //todo: 관리자 인증 로직 추가 예정
        Locale locale = localeResolver.resolveLocale(request);
        AdvertisementUseCase useCase = new AdvertisementUseCase(
            thumbnailImage, detailImage, findLanguageValue(locale.getLanguage()), bannerRequest.getMainTitle(),
            bannerRequest.getSubTitle(), bannerRequest.getContent());
        advertisementBannerService.editAdvertisementBanner(bannerId, useCase);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private static LanguageCode findLanguageValue(String language) {
        if (language.equals("ko")) {
            return LanguageCode.KO;
        }
        if (language.equals("en")) {
            return LanguageCode.EN;
        }
        if (language.equals("ja")) {
            return LanguageCode.JA;
        }
        throw new GeneralException(BannerErrorCode.LANGUAGE_DATA_MISMATCH);
    }
}
