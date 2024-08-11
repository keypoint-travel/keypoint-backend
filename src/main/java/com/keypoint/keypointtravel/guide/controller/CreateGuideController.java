package com.keypoint.keypointtravel.guide.controller;

import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.guide.dto.request.CreateGuideRequest;
import com.keypoint.keypointtravel.guide.dto.request.CreateGuideTranslationRequest;
import com.keypoint.keypointtravel.guide.dto.useCase.CreateGuideTranslationUseCase;
import com.keypoint.keypointtravel.guide.dto.useCase.CreateGuideUseCase;
import com.keypoint.keypointtravel.guide.service.CreateGuideService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/guides")
public class CreateGuideController {

    private final CreateGuideService createGuideService;
    
    @PostMapping("")
    public APIResponseEntity<Void> addGuide(
        @Valid @RequestPart(value = "guide") CreateGuideRequest request,
        @RequestPart(required = false) MultipartFile thumbnailImage
    ) {
        CreateGuideUseCase useCase = CreateGuideUseCase.of(request, thumbnailImage);
        createGuideService.addGuide(useCase);

        return APIResponseEntity.<Void>builder()
                .message("이용 가이드 생성 성공")
                .build();
    }
    
    @GetMapping("{guideId}/translations")
    public APIResponseEntity<Void> addGuideTranslation(
        @RequestParam(value = "guideId") Long guideId,
        @Valid @RequestBody CreateGuideTranslationRequest request
        ) {
        CreateGuideTranslationUseCase useCase = CreateGuideTranslationUseCase.of(guideId, request);
        createGuideService.addGuideTranslation(useCase);

        return APIResponseEntity.<Void>builder()
            .message("이용 가이드 생성 성공")
            .build();
    }
}
