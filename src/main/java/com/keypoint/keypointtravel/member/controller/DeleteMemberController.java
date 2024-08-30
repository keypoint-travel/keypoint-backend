package com.keypoint.keypointtravel.member.controller;

import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.member.dto.useCase.MemberIdUseCase;
import com.keypoint.keypointtravel.member.service.DeleteMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class DeleteMemberController {
    private final DeleteMemberService deleteMemberService;

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @DeleteMapping()
    public APIResponseEntity<Void> deleteMember(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        MemberIdUseCase useCase = MemberIdUseCase.from(userDetails.getId());
        deleteMemberService.deleteMember(useCase);

        return APIResponseEntity.<Void>builder()
                .message("사용자 탈퇴 성공")
                .build();
    }
}
