package com.keypoint.keypointtravel.blocked_member.controller;

import com.keypoint.keypointtravel.blocked_member.dto.BlockedMemberRequest;
import com.keypoint.keypointtravel.blocked_member.dto.BlockedMemberUseCase;
import com.keypoint.keypointtravel.blocked_member.service.BlockedMemberService;
import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
