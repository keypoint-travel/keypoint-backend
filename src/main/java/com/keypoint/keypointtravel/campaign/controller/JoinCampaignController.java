package com.keypoint.keypointtravel.campaign.controller;

import com.keypoint.keypointtravel.campaign.dto.useCase.JoinByEmailUseCase;
import com.keypoint.keypointtravel.campaign.service.JoinCampaignService;
import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/campaigns/{campaignId}/participation")
@RequiredArgsConstructor
public class JoinCampaignController {

    private final JoinCampaignService joinCampaignService;

    @PostMapping("/email")
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    public ResponseEntity<Void> joinByEmail(
        @PathVariable("campaignId") Long campaignId,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        joinCampaignService.joinByEmail(
            new JoinByEmailUseCase(userDetails.getId(), userDetails.getEmail(), campaignId));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
