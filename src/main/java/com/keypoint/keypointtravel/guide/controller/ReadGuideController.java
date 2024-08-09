package com.keypoint.keypointtravel.guide.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/guides")
public class ReadGuideController {


    @GetMapping("")
    public APIResponseEntity<?> findGuides(
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        return APIResponseEntity.<?>builder()
                .message("이용가이드 리스트 조회 성공")
                .build();
    }
    
    @GetMapping("{guideId}")
    public APIResponseEntity<?> findGuide(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestParam(value = "guideId") Long guideId
    ) {

        return APIResponseEntity.<?>builder()
                .message("이용가이드 단일 조회 성공")
                .build();
    }
    
    @GetMapping("/management")
    public APIResponseEntity<?> findGuidesInAdmin() {

        return APIResponseEntity.<?>builder()
                .message("이용가이드 리스트 조회 성공")
                .build();
    }
    
    @GetMapping("/management{guideId}")
    public APIResponseEntity<?> findGuideInAdmin(
        @RequestParam(value = "guideId") Long guideId
        ) {

        return APIResponseEntity.<?>builder()
            .message("이용가이드 단일 조회 성공")
            .build();
    }
}
