package com.keypoint.keypointtravel.campaign.controller;

import com.keypoint.keypointtravel.campaign.dto.dto.CampaignInfoDto;
import com.keypoint.keypointtravel.campaign.dto.response.CampaignResponse;
import com.keypoint.keypointtravel.campaign.dto.useCase.CompleteCampaignUseCase;
import com.keypoint.keypointtravel.campaign.service.CompleteCampaignService;
import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/campaigns")
@RequiredArgsConstructor
public class CompleteCampaignController {

    private final CompleteCampaignService completeCampaignService;

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PostMapping("/{campaignId}/completion")
    public ResponseEntity<Void> completeCampaign(
        @PathVariable Long campaignId,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        completeCampaignService.completeCampaign(new CompleteCampaignUseCase(campaignId, userDetails.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("/completion")
    public APIResponseEntity<List<CampaignResponse>> findCampaigns(
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        // 캠페인 정보 조회
        List<CampaignInfoDto> dtoList = completeCampaignService.findCampaigns(userDetails.getId());
        // 응답
        List<CampaignResponse> response = CampaignResponse.from(dtoList);
        return APIResponseEntity.<List<CampaignResponse>>builder()
            .message("종료된 캠페인 목록 조회 성공")
            .data(response)
            .build();
    }
}
