package com.keypoint.keypointtravel.guide.controller;

import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.guide.dto.useCase.DeleteGuideGuideUseCase;
import com.keypoint.keypointtravel.guide.dto.useCase.DeleteGuideTranslationUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/guides")
public class DeleteGuideController {

    @DeleteMapping("")
    public APIResponseEntity<Void> deleteGuide(
        @RequestParam(value = "ids") Long[] ids
    ) {
        DeleteGuideGuideUseCase useCase = DeleteGuideGuideUseCase.from(ids);
        return APIResponseEntity.<Void>builder()
                .message("이용 가이드 삭제 성공")
                .build();
    }
    
    @GetMapping("{guideId}")
    public APIResponseEntity<Void> deleteGuideTranslation(
        @RequestParam(value = "guide-translation-id") Long[] ids
        ) {
        DeleteGuideTranslationUseCase useCase = DeleteGuideTranslationUseCase.from(ids);
        return APIResponseEntity.<Void>builder()
            .message("이용 가이드 삭제 성공")
            .build();
    }
}
