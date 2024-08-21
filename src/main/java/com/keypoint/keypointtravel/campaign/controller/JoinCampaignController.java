package com.keypoint.keypointtravel.campaign.controller;

import com.keypoint.keypointtravel.campaign.dto.request.JoinByCampaignCodeRequest;
import com.keypoint.keypointtravel.campaign.dto.request.ApproveByCodeRequest;
import com.keypoint.keypointtravel.campaign.dto.useCase.ApproveByCodeUseCase;
import com.keypoint.keypointtravel.campaign.dto.useCase.JoinByCodeUseCase;
import com.keypoint.keypointtravel.campaign.dto.useCase.JoinByEmailUseCase;
import com.keypoint.keypointtravel.campaign.service.JoinCampaignService;
import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
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
public class JoinCampaignController {

    private final JoinCampaignService joinCampaignService;

    @PostMapping("/{campaignId}/participation/email")
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    public ResponseEntity<Void> joinByEmail(
        @PathVariable("campaignId") Long campaignId,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        joinCampaignService.joinByEmail(
            new JoinByEmailUseCase(userDetails.getId(), userDetails.getEmail(), campaignId));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/participation/code")
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    public ResponseEntity<Void> requestJoinByCampaignCode(
        @RequestBody @Valid JoinByCampaignCodeRequest request,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        joinCampaignService.requestJoinByCampaignCode(
            new JoinByCodeUseCase(userDetails.getId(), request.getCampaignCode()));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/participation/approval")
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    public ResponseEntity<Void> approveJoinByCampaignCode(
        @RequestBody @Valid ApproveByCodeRequest request,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        // 캠페인 장만 접근 가능
        joinCampaignService.approveJoinByCampaignCode(new ApproveByCodeUseCase(userDetails.getId(), request.isApprove(),
            request.getMemberId(), request.getCampaignId()));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
