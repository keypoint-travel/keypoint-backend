package com.keypoint.keypointtravel.banner.controller;

import com.keypoint.keypointtravel.banner.dto.dto.AdvertisementBannerDto;
import com.keypoint.keypointtravel.banner.dto.dto.AdvertisementDetailDto;
import com.keypoint.keypointtravel.banner.dto.request.AdvertisementRequest;
import com.keypoint.keypointtravel.banner.dto.response.*;
import com.keypoint.keypointtravel.banner.dto.useCase.*;
import com.keypoint.keypointtravel.banner.dto.useCase.advertisement.AdvertisementUseCase;
import com.keypoint.keypointtravel.banner.dto.useCase.advertisement.EditAdvertisementUseCase;
import com.keypoint.keypointtravel.banner.dto.useCase.advertisement.EditLocaleAdvertisementUseCase;
import com.keypoint.keypointtravel.banner.service.AdvertisementBannerService;
import com.keypoint.keypointtravel.global.annotation.ValidEnum;
import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.member.entity.Member;
import com.keypoint.keypointtravel.member.service.ReadMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/banners/advertisement")
@RequiredArgsConstructor
public class AdvertisementBannerController {

    private final AdvertisementBannerService advertisementBannerService;

    private final ReadMemberService readMemberService;

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
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        //todo: 관리자 인증 로직 추가 예정
        AdvertisementUseCase useCase = new AdvertisementUseCase(thumbnailImage, detailImage,
            bannerRequest.getLanguageCode(), bannerRequest.getMainTitle(),
            bannerRequest.getSubTitle(), bannerRequest.getContent());
        advertisementBannerService.saveAdvertisementBanner(useCase);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 이미 생성된 배너에 다른 언어로 추가
    @PostMapping("/{bannerId}")
    public ResponseEntity<Void> saveAdvertisementBanner(
        @PathVariable(value = "bannerId", required = false) Long bannerId,
        @RequestBody @Valid AdvertisementRequest bannerRequest,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        //todo: 관리자 인증 로직 추가 예정
        PlusAdvertisementUseCase useCase = new PlusAdvertisementUseCase(bannerId,
            bannerRequest.getMainTitle(), bannerRequest.getSubTitle(), bannerRequest.getContent(),
            bannerRequest.getLanguageCode());
        advertisementBannerService.saveBannerByOtherLanguage(useCase);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public APIResponseEntity<AdvertisementBannerListResponse> findAdvertisementBannerList(
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        //todo: 관리자 인증 로직 추가 예정
        List<AdvertisementBannerDto> dtoList = advertisementBannerService.findAdvertisementBanners();
        AdvertisementBannerListResponse response = new AdvertisementBannerListResponse(dtoList);
        return APIResponseEntity.<AdvertisementBannerListResponse>builder()
            .message("광고 배너 목록 조회 성공")
            .data(response)
            .build();
    }

    @GetMapping("/management/{bannerId}")
    public APIResponseEntity<ManagementAdvBannerResponse> findAdvertisementBannerDetails(
        @PathVariable("bannerId") Long bannerId,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        //todo: 관리자 인증 로직 추가 예정
        ManagementAdvBannerResponse response = advertisementBannerService.findAdvertisementBanner(bannerId);
        return APIResponseEntity.<ManagementAdvBannerResponse>builder()
            .message("(관리자)광고 배너 상세페이지 조회 성공")
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
        @RequestParam("languageCode") @ValidEnum(enumClass = LanguageCode.class) LanguageCode languageCode,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        //todo: 관리자 인증 로직 추가 예정
        advertisementBannerService.deleteBanner(new DeleteUseCase(bannerId, languageCode));
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

    @PatchMapping("/{bannerId}/locale")
    public ResponseEntity<Void> editAdvertisementBanner(
        @PathVariable("bannerId") Long bannerId,
        @RequestBody @Valid AdvertisementRequest bannerRequest,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        //todo: 관리자 인증 로직 추가 예정
        EditLocaleAdvertisementUseCase useCase = new EditLocaleAdvertisementUseCase(
            bannerRequest.getLanguageCode(), bannerRequest.getMainTitle(),
            bannerRequest.getSubTitle(), bannerRequest.getContent());
        advertisementBannerService.editLocaleAdvertisementBanner(bannerId, useCase);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{bannerId}")
    public ResponseEntity<Void> editAdvertisementBannerImage(
        @PathVariable("bannerId") Long bannerId,
        @RequestPart(value = "thumbnailImage", required = false) MultipartFile thumbnailImage,
        @RequestPart(value = "detailImage", required = false) MultipartFile detailImage,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        //todo: 관리자 인증 로직 추가 예정
        EditAdvertisementUseCase useCase = new EditAdvertisementUseCase(thumbnailImage,
            detailImage);
        advertisementBannerService.editAdvertisementBanner(bannerId, useCase);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
