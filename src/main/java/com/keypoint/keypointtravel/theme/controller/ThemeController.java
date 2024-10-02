package com.keypoint.keypointtravel.theme.controller;

import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.global.dto.response.PageResponse;
import com.keypoint.keypointtravel.global.dto.useCase.PageUseCase;
import com.keypoint.keypointtravel.theme.dto.request.ThemeRequest;
import com.keypoint.keypointtravel.theme.dto.response.ThemeResponse;
import com.keypoint.keypointtravel.theme.dto.useCase.CreateThemeUseCase;
import com.keypoint.keypointtravel.theme.dto.useCase.DeleteThemeUseCase;
import com.keypoint.keypointtravel.theme.dto.useCase.UpdateThemeUseCase;
import com.keypoint.keypointtravel.theme.service.ThemeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/themes")
public class ThemeController {
    private final ThemeService themeService;

    @PostMapping
    public APIResponseEntity<Void> saveTheme(
        @Valid @RequestBody ThemeRequest request,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        //todo: 관리자 인증 로직 추가 예정;
        CreateThemeUseCase useCase = CreateThemeUseCase.of(request);
        themeService.saveTheme(useCase);
        return APIResponseEntity.<Void>builder()
            .message("무료 테마 등록 성공")
            .build();
    }


    @PutMapping("/{themeId}")
    public APIResponseEntity<Void> updateTheme(
        @PathVariable("themeId") Long themeId,
        @Valid @RequestBody ThemeRequest request,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        //todo: 관리자 인증 로직 추가 예정
        UpdateThemeUseCase useCase = UpdateThemeUseCase.of(themeId, request);
        themeService.updateTheme(useCase);
        return APIResponseEntity.<Void>builder()
            .message("무료 테마 수정 성공")
            .build();
    }

    @DeleteMapping()
    public APIResponseEntity<Void> deleteThemes(
        @RequestParam("theme-ids") Long[] themeIds,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        //todo: 관리자 인증 로직 추가 예정
        DeleteThemeUseCase useCase = DeleteThemeUseCase.from(
            themeIds
        );
        themeService.deleteThemes(useCase);

        return APIResponseEntity.<Void>builder()
            .message("무료 테마 삭제 성공")
            .build();
    }

    /**
     * @param sortBy createAt
     * @param direction asc, desc
     * @param pageable
     * @return
     */
    @GetMapping
    public APIResponseEntity<PageResponse<ThemeResponse>> findThemes(
        @RequestParam(name = "sort-by", required = false) String sortBy,
        @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
        @PageableDefault(sort = "createAt", direction = Direction.ASC) Pageable pageable
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
        Page<ThemeResponse> result = themeService.findThemes(useCase);

        return APIResponseEntity.toPage(
            "무료 테마 목록 조회 성공",
            result
        );
    }


}
