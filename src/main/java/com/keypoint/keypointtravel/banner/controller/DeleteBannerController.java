package com.keypoint.keypointtravel.banner.controller;


import com.keypoint.keypointtravel.banner.dto.useCase.DeleteUseCase;
import com.keypoint.keypointtravel.banner.service.DeleteBannerService;
import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/banners")
@RequiredArgsConstructor
public class DeleteBannerController {

    private final DeleteBannerService deleteBannerService;

    @DeleteMapping("/{bannerId}")
    public ResponseEntity<Void> deleteBanner(
        @PathVariable("bannerId") Long bannerId,
        @RequestParam(value = "language", required = false) String language,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        //todo: 관리자 인증 로직 추가 예정
        deleteBannerService.deleteBanner(new DeleteUseCase(bannerId, language));
        return ResponseEntity.noContent().build();
    }
}