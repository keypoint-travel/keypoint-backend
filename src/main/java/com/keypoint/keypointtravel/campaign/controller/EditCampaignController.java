package com.keypoint.keypointtravel.campaign.controller;

import com.keypoint.keypointtravel.campaign.dto.request.createRequest.EditRequest;
import com.keypoint.keypointtravel.campaign.dto.response.EditCampaignResponse;
import com.keypoint.keypointtravel.campaign.dto.useCase.FIndCampaignUseCase;
import com.keypoint.keypointtravel.campaign.dto.useCase.UpdateUseCase;
import com.keypoint.keypointtravel.campaign.service.EditCampaignService;
import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/campaigns/{campaignId}")
@RequiredArgsConstructor
public class EditCampaignController {

    private final EditCampaignService editCampaignService;

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping
    public APIResponseEntity<EditCampaignResponse> findCampaign(
        @PathVariable Long campaignId,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        FIndCampaignUseCase useCase = new FIndCampaignUseCase(campaignId, userDetails.getId());
        EditCampaignResponse response = editCampaignService.findCampaign(useCase);
        return APIResponseEntity.<EditCampaignResponse>builder()
            .message("캠페인 조회 성공")
            .data(response)
            .build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PutMapping
    public ResponseEntity<Void> editCampaign(
        @PathVariable Long campaignId,
        @RequestPart(value = "coverImage", required = false) MultipartFile coverImage,
        @RequestPart(value = "detail") @Valid EditRequest request,
        @AuthenticationPrincipal CustomUserDetails userDetails) {

        editCampaignService.editCampaign(UpdateUseCase.of(campaignId, coverImage, request, userDetails.getId()));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
