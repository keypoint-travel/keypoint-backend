package com.keypoint.keypointtravel.campaign.controller;

import com.keypoint.keypointtravel.campaign.dto.useCase.CompleteCampaignUseCase;
import com.keypoint.keypointtravel.campaign.service.CompleteCampaignService;
import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/campaigns")
@RequiredArgsConstructor
public class CompleteCampaignController {

    private final CompleteCampaignService completeCampaignService;

    @PostMapping("/{campaignId}/completion")
    public ResponseEntity<Void> completeCampaign(
        @PathVariable Long campaignId,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        completeCampaignService.completeCampaign(new CompleteCampaignUseCase(campaignId, userDetails.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
