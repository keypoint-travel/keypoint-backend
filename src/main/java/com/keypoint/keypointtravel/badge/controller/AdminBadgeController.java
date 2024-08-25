package com.keypoint.keypointtravel.badge.controller;

import com.keypoint.keypointtravel.badge.dto.request.CreateBadgeRequest;
import com.keypoint.keypointtravel.badge.dto.useCase.CreateBadgeUseCase;
import com.keypoint.keypointtravel.badge.service.AdminBadgeService;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/badges")
@RequiredArgsConstructor
public class AdminBadgeController {

    private final AdminBadgeService adminBadgeService;

    @PostMapping()
    public APIResponseEntity<Void> addBadge(
        @Valid @RequestPart(value = "badge") CreateBadgeRequest request,
        @RequestPart(value = "badgeOnImage", required = false) MultipartFile badgeOnImage,
        @RequestPart(value = "badgeOffImage", required = false) MultipartFile badgeOffImage
    ) {
        CreateBadgeUseCase useCase = CreateBadgeUseCase.of(
            request,
            badgeOnImage,
            badgeOffImage
        );
        adminBadgeService.addBadge(useCase);

        return APIResponseEntity.<Void>builder()
            .message("배지 생성 성공")
            .build();
    }
}
