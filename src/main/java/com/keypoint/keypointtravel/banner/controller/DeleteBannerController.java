package com.keypoint.keypointtravel.banner.controller;


import com.keypoint.keypointtravel.banner.dto.useCase.DeleteUseCase;
import com.keypoint.keypointtravel.banner.service.DeleteBannerService;
import com.keypoint.keypointtravel.global.annotation.ValidEnum;
import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/banners")
@RequiredArgsConstructor
public class DeleteBannerController {

    private final DeleteBannerService deleteBannerService;

    // bannerId에 해당하는 전체 배너 삭제
    @DeleteMapping("/{bannerId}")
    public ResponseEntity<Void> deleteBanners(
        @PathVariable("bannerId") Long bannerId,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        //todo: 관리자 인증 로직 추가 예정
        deleteBannerService.deleteBanner(new DeleteUseCase(bannerId, null));
        return ResponseEntity.noContent().build();
    }

    // bannerId에 해당하는 특정 배너 삭제
    @DeleteMapping("/{bannerId}/locale")
    public ResponseEntity<Void> deleteBanner(
        @PathVariable("bannerId") Long bannerId,
        @RequestParam("languageCode") @ValidEnum(enumClass = LanguageCode.class) LanguageCode languageCode,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        //todo: 관리자 인증 로직 추가 예정
        deleteBannerService.deleteBanner(new DeleteUseCase(bannerId, languageCode));
        return ResponseEntity.noContent().build();
    }
}