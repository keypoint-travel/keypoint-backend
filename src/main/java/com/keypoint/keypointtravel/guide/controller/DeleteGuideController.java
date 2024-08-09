package com.keypoint.keypointtravel.guide.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/guides")
public class DeleteGuideController {
    @DeleteMapping("")
    public APIResponseEntity<?> deleteGuide(
        @RequestParam(value = "guide-id") Long[] guideIds
    ) {

        return APIResponseEntity.<?>builder()
                .message("이용 가이드 삭제 성공")
                .build();
    }
    
    @GetMapping("{guideId}")
    public APIResponseEntity<?> deleteGuideTranslation(
        @RequestParam(value = "guide-translation-id") Long[] guideTranslationIds
        ) {

        return APIResponseEntity.<?>builder()
            .message("이용 가이드 삭제 성공")
            .build();
    }
}
