package com.keypoint.keypointtravel.badge.controller;

import com.keypoint.keypointtravel.badge.dto.useCase.BadgeTypeUseCase;
import com.keypoint.keypointtravel.badge.service.MemberBadgeService;
import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.global.enumType.setting.BadgeType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/earned-badges")
@RequiredArgsConstructor
public class EarnedBadgeController {
    private final MemberBadgeService memberBadgeService;

    @DeleteMapping
    public APIResponseEntity<Void> deleteEarnBadge(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(value = "badge-type") BadgeType type
    ) {
        BadgeTypeUseCase useCase = BadgeTypeUseCase.of(
                userDetails.getId(), type
        );
        memberBadgeService.deleteEarnBadge(useCase);

        return APIResponseEntity.<Void>builder()
                .message("발급 받은 배지 삭제 성공")
                .build();
    }
}
