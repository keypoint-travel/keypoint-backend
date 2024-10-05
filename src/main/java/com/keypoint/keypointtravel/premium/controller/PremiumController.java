package com.keypoint.keypointtravel.premium.controller;

import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.premium.dto.response.RemainingPeriodResponse;
import com.keypoint.keypointtravel.premium.dto.useCase.PremiumMemberUseCase;
import com.keypoint.keypointtravel.premium.service.PremiumService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/premiums")
@RequiredArgsConstructor
public class PremiumController {

    private final PremiumService premiumService;

    // 남은 프리미엄 기간 조회
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("/remaining-days")
    public APIResponseEntity<RemainingPeriodResponse> findRemainingPremiumDays(
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        RemainingPeriodResponse response = premiumService.findRemainingPremiumDays(
            new PremiumMemberUseCase(userDetails.getId()));
        return APIResponseEntity.<RemainingPeriodResponse>builder()
            .message("남은 프리미엄 기간 조회 성공")
            .data(response)
            .build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PostMapping("/free-trial")
    public APIResponseEntity<Void> startFreeTrial(
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        premiumService.startFreeTrial(new PremiumMemberUseCase(userDetails.getId()));
        return APIResponseEntity.<Void>builder()
            .message("무료체험 시작 성공")
            .build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PostMapping
    public APIResponseEntity<Void> startPremium(
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        premiumService.updateMemberPremium(new PremiumMemberUseCase(userDetails.getId()));
        return APIResponseEntity.<Void>builder()
            .message("프리미엄 적용 성공")
            .build();
    }
}
