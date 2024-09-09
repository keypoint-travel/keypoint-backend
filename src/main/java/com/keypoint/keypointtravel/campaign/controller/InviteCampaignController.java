package com.keypoint.keypointtravel.campaign.controller;

import com.keypoint.keypointtravel.campaign.dto.request.InviteByEmailRequest;
import com.keypoint.keypointtravel.campaign.dto.request.InviteFriendRequest;
import com.keypoint.keypointtravel.campaign.dto.response.FindInvitationResponse;
import com.keypoint.keypointtravel.campaign.dto.useCase.FIndCampaignUseCase;
import com.keypoint.keypointtravel.campaign.dto.useCase.CampaignEmailUseCase;
import com.keypoint.keypointtravel.campaign.dto.useCase.InviteFriendUseCase;
import com.keypoint.keypointtravel.campaign.service.InviteCampaignService;
import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/campaigns")
@RequiredArgsConstructor
public class InviteCampaignController {

    private final InviteCampaignService inviteCampaignService;


    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("/{campaignId}/invitation")
    public APIResponseEntity<FindInvitationResponse> findInvitationView(
        @PathVariable Long campaignId,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        FindInvitationResponse response = inviteCampaignService.findInvitationView(
            new FIndCampaignUseCase(campaignId, userDetails.getId()));
        return APIResponseEntity.<FindInvitationResponse>builder()
            .message("캠페인 초대 화면 조회 성공")
            .data(response)
            .build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PostMapping("/invitation/email")
    public ResponseEntity<Void> inviteByEmail(
        @RequestBody @Valid InviteByEmailRequest request,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        CampaignEmailUseCase useCase = new CampaignEmailUseCase(
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
    @PostMapping("/invitation/friend")
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
