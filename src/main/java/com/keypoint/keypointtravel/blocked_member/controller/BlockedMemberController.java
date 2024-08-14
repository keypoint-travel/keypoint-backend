package com.keypoint.keypointtravel.blocked_member.controller;

import com.keypoint.keypointtravel.blocked_member.dto.BlockedMemberResponse;
import com.keypoint.keypointtravel.blocked_member.dto.BlockedMemberUseCase;
import com.keypoint.keypointtravel.blocked_member.dto.BlockedMemberInfo;
import com.keypoint.keypointtravel.blocked_member.service.BlockedMemberService;
import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/blocked-members")
@RequiredArgsConstructor
public class BlockedMemberController {

    private final BlockedMemberService blockedMemberService;

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PostMapping("/{blockedMemberId}")
    public ResponseEntity<Void> blockMember(@PathVariable Long blockedMemberId,
                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        blockedMemberService.blockMember(new BlockedMemberUseCase(blockedMemberId, userDetails.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PostMapping("/unblock/{blockedMemberId}")
    public ResponseEntity<Void> unblockMember(@PathVariable Long blockedMemberId,
                                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        blockedMemberService.unblockMember(new BlockedMemberUseCase(blockedMemberId, userDetails.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping
    public APIResponseEntity<BlockedMemberResponse> findBlockedMemberList(
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<BlockedMemberInfo> useCases = blockedMemberService.findBlockedMemberList(userDetails.getId());
        return APIResponseEntity.<BlockedMemberResponse>builder()
            .message("차단된 회원 목록 조회 성공")
            .data(new BlockedMemberResponse(useCases))
            .build();
    }
}
