package com.keypoint.keypointtravel.campaign.controller;

import com.keypoint.keypointtravel.campaign.dto.response.CampaignResponse;
import com.keypoint.keypointtravel.campaign.dto.useCase.CompleteCampaignUseCase;
import com.keypoint.keypointtravel.campaign.dto.useCase.FIndCampaignListUseCase;
import com.keypoint.keypointtravel.campaign.service.CompleteCampaignService;
import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.global.dto.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/campaigns")
@RequiredArgsConstructor
public class CompleteCampaignController {

    private final CompleteCampaignService completeCampaignService;

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PostMapping("/{campaignId}/completion")
    public ResponseEntity<Void> completeCampaign(
        @PathVariable(value = "campaignId") Long campaignId,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        completeCampaignService.completeCampaign(new CompleteCampaignUseCase(campaignId, userDetails.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("/completion")
    public APIResponseEntity<PageResponse<CampaignResponse>> findCampaigns(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestParam(value = "size", defaultValue = "10") int size,
        @RequestParam(value = "page", defaultValue = "1") int page) {
        // 캠페인 정보
        FIndCampaignListUseCase useCase = new FIndCampaignListUseCase(userDetails.getId(), size, page);
        Page<CampaignResponse> response = completeCampaignService.findCampaigns(useCase);
        // 응답
        return APIResponseEntity.toPage(
            "종료된 캠페인 목록 조회 성공",
            response);
    }
}
