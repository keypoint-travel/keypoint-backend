package com.keypoint.keypointtravel.guide.controller;

import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.guide.dto.response.ReadGuideDetailResponse;
import com.keypoint.keypointtravel.guide.dto.response.ReadGuideInAdminResponse;
import com.keypoint.keypointtravel.guide.dto.response.ReadGuideResponse;
import com.keypoint.keypointtravel.guide.dto.response.readGuideDetailInAdmin.ReadGuideDetailInAdminResponse;
import com.keypoint.keypointtravel.guide.dto.useCase.GuideIdUseCase;
import com.keypoint.keypointtravel.guide.dto.useCase.ReadGuideInAdminUseCase;
import com.keypoint.keypointtravel.guide.dto.useCase.ReadGuideTranslationIdUseCase;
import com.keypoint.keypointtravel.guide.service.ReadGuideService;
import com.keypoint.keypointtravel.member.dto.useCase.MemberIdAndPageableUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/guides")
public class ReadGuideController {

    private final ReadGuideService readGuideService;

    @GetMapping("")
    public APIResponseEntity<Slice<ReadGuideResponse>> findGuides(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PageableDefault(size = 15, sort = "order", direction = Direction.ASC) Pageable pageable
    ) {
        MemberIdAndPageableUseCase useCase = MemberIdAndPageableUseCase.of(
            userDetails.getId(),
            pageable
        );
        Slice<ReadGuideResponse> result = readGuideService.findGuides(useCase);
        return APIResponseEntity.<Slice<ReadGuideResponse>>builder()
            .message("이용가이드 리스트 조회 성공")
            .data(result)
            .build();
    }

    @GetMapping("{guideTranslationId}")
    public APIResponseEntity<ReadGuideDetailResponse> findGuideDetail(
        @PathVariable(value = "guideTranslationId") Long guideTranslationIds
    ) {
        ReadGuideTranslationIdUseCase useCase = ReadGuideTranslationIdUseCase.from(
            guideTranslationIds
        );
        ReadGuideDetailResponse result = readGuideService.findGuideDetail(useCase);
        return APIResponseEntity.<ReadGuideDetailResponse>builder()
            .message("이용가이드 단일 조회 성공")
            .data(result)
            .build();
    }

    @GetMapping("/management")
    public APIResponseEntity<Page<ReadGuideInAdminResponse>> findGuidesInAdmin(
        @PageableDefault(size = 15, sort = "order", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        ReadGuideInAdminUseCase useCase = ReadGuideInAdminUseCase.from(pageable);
        Page<ReadGuideInAdminResponse> result = readGuideService.findGuidesInAdmin(useCase);
        return APIResponseEntity.<Page<ReadGuideInAdminResponse>>builder()
            .message("이용가이드 리스트 조회 성공")
            .data(result)
            .build();
    }

    @GetMapping("/management/{guideId}")
    public APIResponseEntity<ReadGuideDetailInAdminResponse> findGuideDetailInAdmin(
        @PathVariable(value = "guideId") Long guideId
    ) {
        GuideIdUseCase useCase = GuideIdUseCase.from(guideId);
        ReadGuideDetailInAdminResponse result = readGuideService.findGuideDetailInAdmin(useCase);
        return APIResponseEntity.<ReadGuideDetailInAdminResponse>builder()
            .message("이용가이드 단일 조회 성공")
            .data(result)
            .build();
    }
}
