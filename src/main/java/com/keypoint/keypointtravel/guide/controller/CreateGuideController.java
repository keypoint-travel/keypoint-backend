package com.keypoint.keypointtravel.guide.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/guides")
public class CreateGuideController {
    
    @PostMapping("")
    public APIResponseEntity<?> addGuide() {

        return APIResponseEntity.<?>builder()
                .message("이용 가이드 생성 성공")
                .build();
    }
    
    @GetMapping("{guideId}/translations")
    public APIResponseEntity<?> addGuideTranslation(
        @RequestParam(value = "guideId") Long guideId
        ) {

        return APIResponseEntity.<?>builder()
            .message("이용 가이드 생성 성공")
            .build();
    }
}
