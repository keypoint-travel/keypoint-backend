package com.keypoint.keypointtravel.blocked_member.controller;

import com.keypoint.keypointtravel.blocked_member.dto.BlockedMemberRequest;
import com.keypoint.keypointtravel.blocked_member.dto.BlockedMemberResponse;
import com.keypoint.keypointtravel.blocked_member.dto.BlockedMemberUseCase;
import com.keypoint.keypointtravel.blocked_member.dto.BlockedMemberInfo;
import com.keypoint.keypointtravel.blocked_member.service.BlockedMemberService;
import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/blocked-members")
@RequiredArgsConstructor
public class BlockedMemberController {

    private final BlockedMemberService blockedMemberService;

    @PostMapping
    public ResponseEntity<Void> blockMember(@RequestBody BlockedMemberRequest request,
                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        blockedMemberService.blockMember(new BlockedMemberUseCase(request.getBlockedMemberId(), userDetails.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/unblock")
    public ResponseEntity<Void> unblockMember(@RequestBody BlockedMemberRequest request,
                                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        blockedMemberService.unblockMember(new BlockedMemberUseCase(request.getBlockedMemberId(), userDetails.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

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
