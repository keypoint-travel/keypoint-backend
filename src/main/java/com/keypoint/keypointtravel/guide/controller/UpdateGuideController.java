package com.keypoint.keypointtravel.guide.controller;

import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.guide.dto.request.updateGuide.UpdateGuideRequest;
import com.keypoint.keypointtravel.guide.dto.request.updateGuide.UpdateGuideTranslationRequest;
import com.keypoint.keypointtravel.guide.dto.useCase.updateGuide.UpdateGuideTranslationUseCase;
import com.keypoint.keypointtravel.guide.dto.useCase.updateGuide.UpdateGuideUseCase;
import com.keypoint.keypointtravel.guide.service.UpdateGuideService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/guides")
public class UpdateGuideController {

    private final UpdateGuideService updateGuideService;

    @PutMapping("/{guideId}")
    public APIResponseEntity<Void> updateGuide(
        @PathVariable(value = "guideId") Long guideId,
        @Valid @RequestPart(value = "guide") UpdateGuideRequest request,
        @RequestPart(required = false, value = "thumbnailImage") MultipartFile thumbnailImage
    ) {
        UpdateGuideUseCase useCase = UpdateGuideUseCase.of(guideId, request, thumbnailImage);
        updateGuideService.updateGuide(useCase);

        return APIResponseEntity.<Void>builder()
            .message("이용 가이드 수정 성공")
            .build();
    }

    @PutMapping("/{guideId}/translations/{guideTranslationId}")
    public APIResponseEntity<Void> updateGuideTranslation(
        @PathVariable(value = "guideId") Long guideId,
        @PathVariable(value = "guideTranslationId") Long guideTranslationId,
        @Valid @RequestBody UpdateGuideTranslationRequest request
    ) {
        UpdateGuideTranslationUseCase useCase = UpdateGuideTranslationUseCase.of(
            guideId,
            guideTranslationId,
            request
        );
        updateGuideService.updateGuideTranslation(useCase);

        return APIResponseEntity.<Void>builder()
            .message("특정 이용 가이드 번역 정보 수정 성공")
            .build();
    }
}
