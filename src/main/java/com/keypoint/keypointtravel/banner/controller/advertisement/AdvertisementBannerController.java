package com.keypoint.keypointtravel.banner.controller.advertisement;

import com.keypoint.keypointtravel.banner.dto.response.ImageUrlResponse;
import com.keypoint.keypointtravel.banner.dto.useCase.ImageUseCase;
import com.keypoint.keypointtravel.banner.service.AdvertisementBannerService;
import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/banners/advertisement")
@RequiredArgsConstructor
public class AdvertisementBannerController {

    private final AdvertisementBannerService advertisementBannerService;

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
}
