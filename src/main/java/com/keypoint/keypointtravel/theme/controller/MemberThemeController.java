package com.keypoint.keypointtravel.theme.controller;

import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.member.dto.useCase.MemberIdUseCase;
import com.keypoint.keypointtravel.theme.dto.request.MemberThemeRequest;
import com.keypoint.keypointtravel.theme.dto.response.MemberThemeResponse;
import com.keypoint.keypointtravel.theme.dto.useCase.MemberThemeUseCase;
import com.keypoint.keypointtravel.theme.service.MemberThemeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/themes")
public class MemberThemeController {
    private final MemberThemeService memberThemeService;

    @PatchMapping("/select")
    public APIResponseEntity<Void> updateTheme(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestBody MemberThemeRequest request
    ) {
        MemberThemeUseCase useCase = MemberThemeUseCase.of(
            userDetails.getId(),
            request.getThemeId(),
            request.isPaid()
        );
        memberThemeService.updateTheme(useCase);

        return APIResponseEntity.<Void>builder()
            .message("테마 설정 성공")
            .build();
    }

    @GetMapping("/member")
    public APIResponseEntity<MemberThemeResponse> findThemeInMember(
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        MemberIdUseCase useCase = MemberIdUseCase.from(
            userDetails.getId()
        );
        MemberThemeResponse result = memberThemeService.findThemeInMember(useCase);

        return APIResponseEntity.<MemberThemeResponse>builder()
            .message("유저 테마 조회 성공")
            .data(result)
            .build();
    }
}
