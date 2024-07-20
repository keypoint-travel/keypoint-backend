package com.keypoint.keypointtravel.banner.controller;

import com.keypoint.keypointtravel.banner.dto.useCase.ChangeLikeUseCase;
import com.keypoint.keypointtravel.banner.service.BannerLikeService;
import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/banners/{bannerId}/like")
@RequiredArgsConstructor
public class BannerLikeController {

    private final BannerLikeService bannerLikeService;

    @PostMapping
    public ResponseEntity<Void> changeLike(
        @PathVariable Long bannerId, @RequestParam() Boolean hasILiked,
        @AuthenticationPrincipal CustomUserDetails userDetails){

        bannerLikeService.changeLike(new ChangeLikeUseCase(bannerId, hasILiked, userDetails.getId()));

        return ResponseEntity.noContent().build();
    }
}
