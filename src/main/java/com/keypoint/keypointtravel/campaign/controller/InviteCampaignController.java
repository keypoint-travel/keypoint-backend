package com.keypoint.keypointtravel.campaign.controller;

import com.keypoint.keypointtravel.campaign.dto.request.InviteByEmailRequest;
import com.keypoint.keypointtravel.campaign.dto.request.InviteFriendRequest;
import com.keypoint.keypointtravel.campaign.dto.useCase.InviteByEmailUseCase;
import com.keypoint.keypointtravel.campaign.dto.useCase.InviteFriendUseCase;
import com.keypoint.keypointtravel.campaign.service.InviteCampaignService;
import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/campaigns/invitation")
@RequiredArgsConstructor
public class InviteCampaignController {

    private final InviteCampaignService inviteCampaignService;

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PostMapping("/email")
    public ResponseEntity<Void> inviteByEmail(
        @RequestBody @Valid InviteByEmailRequest request,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        InviteByEmailUseCase useCase = new InviteByEmailUseCase(
            request.getEmail(),
            userDetails.getId(),
            request.getCampaignId());
        // 초대 가능한지 확인
        inviteCampaignService.validateInvitation(useCase);
        // 이메일로 초대
        inviteCampaignService.sendEmail(useCase);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PostMapping("/friend")
    public ResponseEntity<Void> inviteFriends(
        @RequestBody @Valid InviteFriendRequest request,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        inviteCampaignService.inviteFriends(
            InviteFriendUseCase.of(
                request.getCampaignId(),
                userDetails.getId(),
                request.getFriends()
            ));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
