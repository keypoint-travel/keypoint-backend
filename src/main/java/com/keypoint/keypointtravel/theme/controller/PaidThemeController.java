package com.keypoint.keypointtravel.theme.controller;

import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.theme.dto.request.ThemeRequest;
import com.keypoint.keypointtravel.theme.dto.useCase.CreateThemeUseCase;
import com.keypoint.keypointtravel.theme.dto.useCase.DeleteThemeUseCase;
import com.keypoint.keypointtravel.theme.dto.useCase.UpdateThemeUseCase;
import com.keypoint.keypointtravel.theme.service.PaidThemeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/paid-themes")
public class PaidThemeController {
    private final PaidThemeService paidThemeService;

    @PostMapping
    public APIResponseEntity<Void> savePaidTheme(
        @Valid @RequestBody ThemeRequest request,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        //todo: 관리자 인증 로직 추가 예정;
        CreateThemeUseCase useCase = CreateThemeUseCase.of(request);
        paidThemeService.savePaidTheme(useCase);
        return APIResponseEntity.<Void>builder()
            .message("유료 테마 등록 성공")
            .build();
    }

    @PutMapping("/{paid-themeId}")
    public APIResponseEntity<Void> updateTheme(
        @PathVariable("paid-themeId") Long themeId,
        @Valid @RequestBody ThemeRequest request,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        //todo: 관리자 인증 로직 추가 예정
        UpdateThemeUseCase useCase = UpdateThemeUseCase.of(themeId, request);
        paidThemeService.updateTheme(useCase);
        return APIResponseEntity.<Void>builder()
            .message("유료 테마 수정 성공")
            .build();
    }

    @DeleteMapping()
    public APIResponseEntity<Void> deleteThemes(
        @RequestParam("paid-theme-ids") Long[] themeIds,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        //todo: 관리자 인증 로직 추가 예정
        DeleteThemeUseCase useCase = DeleteThemeUseCase.from(
            themeIds
        );
        paidThemeService.deleteThemes(useCase);

        return APIResponseEntity.<Void>builder()
            .message("유료 테마 삭제 성공")
            .build();
    }

}
