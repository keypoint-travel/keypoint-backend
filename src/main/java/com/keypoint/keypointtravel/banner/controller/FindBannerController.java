package com.keypoint.keypointtravel.banner.controller;

import com.keypoint.keypointtravel.banner.dto.response.BannerListResponse;
import com.keypoint.keypointtravel.banner.dto.response.RecommendationResponse;
import com.keypoint.keypointtravel.banner.dto.response.commonBanner.CommonBannerResponse;
import com.keypoint.keypointtravel.banner.dto.useCase.BannerUseCase;
import com.keypoint.keypointtravel.banner.dto.useCase.CommonTourismUseCase;
import com.keypoint.keypointtravel.banner.dto.useCase.tourListUseCase.TourismListUseCase;
import com.keypoint.keypointtravel.banner.service.FindBannerService;
import com.keypoint.keypointtravel.banner.service.TourismApiService;
import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.global.enumType.error.BannerErrorCode;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.member.entity.Member;
import com.keypoint.keypointtravel.member.service.ReadMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.keypoint.keypointtravel.global.constants.TourismApiConstants.*;

@RestController
@RequestMapping("/api/v1/banners")
@RequiredArgsConstructor
public class FindBannerController {

    private final FindBannerService findBannerService;

    private final TourismApiService tourismApiService;

    private final ReadMemberService readMemberService;

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

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("/{bannerId}/common")
    public APIResponseEntity<CommonBannerResponse> findCommonBanner(
        @PathVariable("bannerId") Long bannerId,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        // 회원의 등록 언어를 가져옴.
        Member member = readMemberService.findMemberById(userDetails.getId());
        LanguageCode languageCode = member.getMemberDetail().getLanguage();
        // DB에 저장된 공통 배너 조회
        CommonTourismUseCase details = findBannerService.findCommonBanner(
            new BannerUseCase(languageCode, bannerId, userDetails));
        // 한국관광공사 api를 통해 주변 관광지 조회
        TourismListUseCase around = tourismApiService.findAround(findLanguageValue(languageCode),
            details.getCommonTourismDto().getLongitude(), details.getCommonTourismDto().getLatitude(), serviceKey);
        return APIResponseEntity.<CommonBannerResponse>builder()
            .message("공통 배너 상세 조회 ")
            .data(CommonBannerResponse.of(details, around.getResponse().getBody().getItems()))
            .build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("/recommendation")
    public APIResponseEntity<RecommendationResponse> findRecommendationBanner(
        @RequestParam("latitude") Double latitude,
        @RequestParam("longitude") Double longitude,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        // 회원의 등록 언어를 가져옴.
        Member member = readMemberService.findMemberById(userDetails.getId());
        LanguageCode languageCode = member.getMemberDetail().getLanguage();
        // 한국관광공사 api를 통해 주변 관광지 조회
        TourismListUseCase around = tourismApiService.findAround(
            findLanguageValue(languageCode), longitude, latitude, serviceKey);
        if (around.getResponse().getBody().getItems().getItem().isEmpty()) {
            throw new GeneralException(BannerErrorCode.NOT_EXISTED_TOURISM);
        }
        return APIResponseEntity.<RecommendationResponse>builder()
            .message("추천 배너 조회")
            .data(RecommendationResponse.of(
                around.getResponse().getBody().getItems().getItem(),
                member.getMemberDetail().getName(), languageCode))
            .build();
    }

    private String findLanguageValue(LanguageCode language) {
        if (language.equals(LanguageCode.KO)) {
            return KOREAN;
        }
        if (language.equals(LanguageCode.EN)) {
            return ENGLISH;
        }
        if (language.equals(LanguageCode.JA)) {
            return JAPANESE;
        }
        throw new GeneralException(BannerErrorCode.LANGUAGE_DATA_MISMATCH);
    }
}