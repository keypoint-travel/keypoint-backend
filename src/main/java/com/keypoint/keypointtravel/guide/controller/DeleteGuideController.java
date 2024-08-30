package com.keypoint.keypointtravel.guide.controller;

import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.guide.dto.useCase.DeleteGuideGuideUseCase;
import com.keypoint.keypointtravel.guide.dto.useCase.DeleteGuideTranslationUseCase;
import com.keypoint.keypointtravel.guide.service.DeleteGuideService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/guides")
public class DeleteGuideController {

    private final DeleteGuideService deleteGuideService;

    @DeleteMapping()
    public APIResponseEntity<Void> deleteGuides(
        @RequestParam(value = "guide-ids") Long[] ids
    ) {
        DeleteGuideGuideUseCase useCase = DeleteGuideGuideUseCase.from(ids);
        deleteGuideService.deleteGuides(useCase);

        return APIResponseEntity.<Void>builder()
            .message("이용 가이드 복수건 삭제 성공")
            .build();
    }

    @DeleteMapping("/{guideId}")
    public APIResponseEntity<Void> deleteGuide(
        @PathVariable(value = "guideId") Long guideId
    ) {
        DeleteGuideGuideUseCase useCase = DeleteGuideGuideUseCase.from(guideId);
        deleteGuideService.deleteGuides(useCase);

        return APIResponseEntity.<Void>builder()
            .message("이용 가이드 단건 삭제 성공")
            .build();
    }

    @DeleteMapping("/{guideId}/translations/{guideTranslationId}")
    public APIResponseEntity<Void> deleteGuideTranslation(
        @PathVariable(value = "guideId") Long guideId,
        @PathVariable(value = "guideTranslationId") Long guideTranslationId
    ) {
        DeleteGuideTranslationUseCase useCase = DeleteGuideTranslationUseCase.of(
            guideId,
            guideTranslationId
        );
        deleteGuideService.deleteGuideTranslations(useCase);

        return APIResponseEntity.<Void>builder()
            .message("이용 가이드 삭제 성공")
            .build();
    }
}
