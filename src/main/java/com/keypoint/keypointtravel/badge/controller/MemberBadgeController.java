package com.keypoint.keypointtravel.badge.controller;

import com.keypoint.keypointtravel.badge.dto.response.badgeInMember.BadgeInMemberResponse;
import com.keypoint.keypointtravel.badge.service.MemberBadgeService;
import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.member.dto.useCase.MemberIdUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/badges")
@RequiredArgsConstructor
public class MemberBadgeController {

    private final MemberBadgeService memberBadgeService;

    @GetMapping()
    public APIResponseEntity<BadgeInMemberResponse> findBadgesInMember(
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        MemberIdUseCase useCase = MemberIdUseCase.from(
            userDetails.getId()
        );
        BadgeInMemberResponse result = memberBadgeService.findBadgesInMember(useCase);

        return APIResponseEntity.<BadgeInMemberResponse>builder()
            .message("배지 페이지 정보 조회 성공")
            .data(result)
            .build();
    }
}
