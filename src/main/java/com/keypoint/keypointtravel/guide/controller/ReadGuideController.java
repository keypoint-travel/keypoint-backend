package com.keypoint.keypointtravel.guide.controller;

import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.global.dto.response.PageResponse;
import com.keypoint.keypointtravel.global.dto.useCase.PageAndMemberIdUseCase;
import com.keypoint.keypointtravel.global.dto.useCase.PageUseCase;
import com.keypoint.keypointtravel.guide.dto.response.ReadGuideInAdminResponse;
import com.keypoint.keypointtravel.guide.dto.response.ReadGuideResponse;
import com.keypoint.keypointtravel.guide.dto.response.readGuideDetail.ReadGuideDetailResponse;
import com.keypoint.keypointtravel.guide.dto.response.readGuideDetailInAdmin.ReadGuideDetailInAdminResponse;
import com.keypoint.keypointtravel.guide.dto.useCase.GuideIdUseCase;
import com.keypoint.keypointtravel.guide.dto.useCase.ReadGuideTranslationIdUseCase;
import com.keypoint.keypointtravel.guide.service.ReadGuideService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/guides")
public class ReadGuideController {

    private final ReadGuideService readGuideService;

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping
    public APIResponseEntity<PageResponse<ReadGuideResponse>> findGuides(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestParam(name = "sort-by", required = false) String sortBy,
        @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
        @PageableDefault(size = 15, sort = "order", direction = Direction.ASC) Pageable pageable
    ) {
        // sortBy를 제공한 경우, direction 에 따라 정렬 객체 생성
        if (sortBy != null && !sortBy.isEmpty()) {
            Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }

        PageAndMemberIdUseCase useCase = PageAndMemberIdUseCase.of(
                userDetails.getId(),
                sortBy,
                direction,
                pageable
        );
        Page<ReadGuideResponse> result = readGuideService.findGuides(useCase);

        return APIResponseEntity.toPage(
            "이용가이드 리스트 조회 성공",
            result
        );
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("{guideTranslationId}")
    public APIResponseEntity<ReadGuideDetailResponse> findGuideDetail(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable(value = "guideTranslationId") Long guideTranslationIds
    ) {
        ReadGuideTranslationIdUseCase useCase = ReadGuideTranslationIdUseCase.of(
            userDetails.getId(),
            guideTranslationIds
        );
        ReadGuideDetailResponse result = readGuideService.findGuideDetail(useCase);
        return APIResponseEntity.<ReadGuideDetailResponse>builder()
            .message("이용가이드 단일 조회 성공")
            .data(result)
            .build();
    }

    @GetMapping("/management")
    public APIResponseEntity<PageResponse<ReadGuideInAdminResponse>> findGuidesInAdmin(
        @RequestParam(name = "sort-by", required = false) String sortBy,
        @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
        @PageableDefault(size = 15, sort = "order", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        // sortBy를 제공한 경우, direction 에 따라 정렬 객체 생성
        if (sortBy != null && !sortBy.isEmpty()) {
            Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }

        PageUseCase useCase = PageUseCase.of(
            sortBy,
            direction,
            pageable
        );

        Page<ReadGuideInAdminResponse> result = readGuideService.findGuidesInAdmin(useCase);

        return APIResponseEntity.toPage(
            "이용가이드 리스트 조회 성공",
            result
        );
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
