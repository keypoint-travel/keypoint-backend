package com.keypoint.keypointtravel.badge.controller;

import com.keypoint.keypointtravel.badge.dto.request.CreateBadgeRequest;
import com.keypoint.keypointtravel.badge.dto.useCase.CreateBadgeUseCase;
import com.keypoint.keypointtravel.badge.dto.useCase.DeleteBadgeUseCase;
import com.keypoint.keypointtravel.badge.dto.useCase.UpdateBadgeUseCase;
import com.keypoint.keypointtravel.badge.service.AdminBadgeService;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @PutMapping("/{badgeId}")
    public APIResponseEntity<Void> updateBadge(
        @PathVariable("badgeId") Long badgeId,
        @Valid @RequestPart(value = "badge") CreateBadgeRequest request,
        @RequestPart(value = "badgeOnImage", required = false) MultipartFile badgeOnImage,
        @RequestPart(value = "badgeOffImage", required = false) MultipartFile badgeOffImage
    ) {
        UpdateBadgeUseCase useCase = UpdateBadgeUseCase.of(
            badgeId,
            request,
            badgeOnImage,
            badgeOffImage
        );
        adminBadgeService.updateBadge(useCase);

        return APIResponseEntity.<Void>builder()
            .message("배지 수정 성공")
            .build();
    }

    @DeleteMapping()
    public APIResponseEntity<Void> deleteBadge(
        @RequestParam("badge-ids") Long[] badgeIds
    ) {
        DeleteBadgeUseCase useCase = DeleteBadgeUseCase.from(
            badgeIds
        );
        adminBadgeService.deleteBadge(useCase);

        return APIResponseEntity.<Void>builder()
            .message("배지 삭제 성공")
            .build();
    }
}
