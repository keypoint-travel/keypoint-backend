package com.keypoint.keypointtravel.banner.controller;

import com.keypoint.keypointtravel.banner.dto.request.EditBannerRequest;
import com.keypoint.keypointtravel.banner.dto.useCase.EditBannerUseCase;
import com.keypoint.keypointtravel.banner.service.EditBannerService;
import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EditBannerController {

    private final EditBannerService editBannerService;

    @PatchMapping("/api/v1/banners/{bannerId}")
    public ResponseEntity<Void> editBanner(
        @PathVariable(value = "bannerId", required = false) Long bannerId,
        @RequestBody EditBannerRequest bannerRequest,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        //todo: 관리자 인증 로직 추가 예정
        editBannerService.editBanner(
            new EditBannerUseCase(bannerId, bannerRequest, bannerRequest.getLanguageCode()));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
