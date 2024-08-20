package com.keypoint.keypointtravel.campaign.controller;

import com.keypoint.keypointtravel.campaign.dto.request.createRequest.CreateRequest;
import com.keypoint.keypointtravel.campaign.dto.useCase.CreateUseCase;
import com.keypoint.keypointtravel.campaign.dto.useCase.InviteByEmailsUseCase;
import com.keypoint.keypointtravel.campaign.service.CreateCampaignService;
import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/campaigns")
@RequiredArgsConstructor
public class CreateCampaignController {

    private final CreateCampaignService createCampaignService;

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PostMapping
    public ResponseEntity<Void> saveCampaign(
        @RequestPart(value = "coverImage", required = false) MultipartFile coverImage,
        @RequestPart(value = "detail") @Valid CreateRequest request,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        // 캠페인 생성
        Long campaignId = createCampaignService.createCampaign(
            CreateUseCase.of(coverImage, request, userDetails.getId()));
        // 이메일로 초대
        if (!request.getEmails().isEmpty()) {
            createCampaignService.sendEmail(
                InviteByEmailsUseCase.of(request.getEmails(), userDetails.getId(), campaignId));
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
