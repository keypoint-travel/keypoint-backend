package com.keypoint.keypointtravel.badge.controller;

import com.keypoint.keypointtravel.badge.dto.request.CreateBadgeRequest;
import com.keypoint.keypointtravel.badge.dto.response.BadgeInAdminResponse;
import com.keypoint.keypointtravel.badge.dto.useCase.BadgeIdUseCase;
import com.keypoint.keypointtravel.badge.dto.useCase.DeleteBadgeUseCase;
import com.keypoint.keypointtravel.badge.dto.useCase.UpdateBadgeUseCase;
import com.keypoint.keypointtravel.badge.service.AdminBadgeService;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.global.dto.response.PageResponse;
import com.keypoint.keypointtravel.global.dto.useCase.PageUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/badges")
@RequiredArgsConstructor
public class AdminBadgeController {

    private final AdminBadgeService adminBadgeService;

    @PutMapping("/{badgeId}")
    public APIResponseEntity<Void> updateBadge(
        @PathVariable("badgeId") Long badgeId,
        @Valid @RequestPart(value = "badge") CreateBadgeRequest request,
        @RequestPart(value = "badgeOnImage", required = false) MultipartFile badgeOnImage,
        @RequestPart(value = "badgeOffImage", required = false) MultipartFile badgeOffImage
    ) {
        UpdateBadgeUseCase useCase = UpdateBadgeUseCase.of(
            badgeId,
            request,
            badgeOnImage,
            badgeOffImage
        );
        adminBadgeService.updateBadge(useCase);

        return APIResponseEntity.<Void>builder()
            .message("배지 수정 성공")
            .build();
    }

    @Deprecated
    @DeleteMapping()
    public APIResponseEntity<Void> deleteBadge(
        @RequestParam("badge-ids") Long[] badgeIds
    ) {
        DeleteBadgeUseCase useCase = DeleteBadgeUseCase.from(
            badgeIds
        );
        adminBadgeService.deleteBadge(useCase);

        return APIResponseEntity.<Void>builder()
            .message("배지 삭제 성공")
            .build();
    }

    @GetMapping("/management/{badgeId}")
    public APIResponseEntity<BadgeInAdminResponse> findBadgeById(
        @PathVariable("badgeId") Long badgeId
    ) {
        BadgeIdUseCase useCase = BadgeIdUseCase.from(
            badgeId
        );
        BadgeInAdminResponse result = adminBadgeService.findBadgeById(useCase);

        return APIResponseEntity.<BadgeInAdminResponse>builder()
            .message("배지 단건 조회 성공")
            .data(result)
            .build();
    }

    /**
     * @param sortBy    name, order
     * @param direction asc, desc
     * @param pageable
     * @return
     */
    @GetMapping("/management")
    public APIResponseEntity<PageResponse<BadgeInAdminResponse>> findBadges(
        @RequestParam(name = "sort-by", required = false) String sortBy,
        @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
        @PageableDefault(sort = "order", direction = Sort.Direction.ASC) Pageable pageable
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
        Page<BadgeInAdminResponse> result = adminBadgeService.findBadges(useCase);

        return APIResponseEntity.toPage(
            "배지 목록 조회 성공",
            result
        );
    }
}
