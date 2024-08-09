package com.keypoint.keypointtravel.guide.controller;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/guides")
public class UpdateGuideController {
    @PutMapping("{guideId}")
    public APIResponseEntity<?> updateGuide(
        @RequestParam(value = "guideId") Long guideId
        ) {

        return APIResponseEntity.<?>builder()
            .message("이용 가이드 수정 성공")
            .build();
    }
}
