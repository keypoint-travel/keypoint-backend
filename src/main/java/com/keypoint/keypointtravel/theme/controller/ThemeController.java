package com.keypoint.keypointtravel.theme.controller;

import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.theme.dto.request.ThemeRequest;
import com.keypoint.keypointtravel.theme.dto.useCase.CreateThemeUseCase;
import com.keypoint.keypointtravel.theme.service.ThemeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @PostMapping
    public APIResponseEntity<Void> savePaidTheme(
        @Valid @RequestBody ThemeRequest request,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        //todo: 관리자 인증 로직 추가 예정;
        CreateThemeUseCase useCase = CreateThemeUseCase.of(request);
        themeService.savePaidTheme(useCase);
        return APIResponseEntity.<Void>builder()
            .message("유료 테마 등록 성공")
            .build();
    }

}
