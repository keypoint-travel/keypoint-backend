package com.keypoint.keypointtravel.campaign.controller;

import com.keypoint.keypointtravel.campaign.dto.response.PercentageResponse;
import com.keypoint.keypointtravel.campaign.dto.response.details.CampaignDetailsResponse;
import com.keypoint.keypointtravel.campaign.dto.useCase.FIndCampaignUseCase;
import com.keypoint.keypointtravel.campaign.dto.useCase.FindPercentangeUseCase;
import com.keypoint.keypointtravel.campaign.service.FindCampaignService;
import com.keypoint.keypointtravel.campaign.service.FindPercentageService;
import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/campaigns/details")
@RequiredArgsConstructor
public class FindCampaignController {

    private final FindPercentageService findPercentageService;

    private final FindCampaignService findCampaignService;

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("/{campaignId}")
    public APIResponseEntity<CampaignDetailsResponse> findCampaignDetails(
        @PathVariable Long campaignId,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        FIndCampaignUseCase useCase = new FIndCampaignUseCase(campaignId, userDetails.getId());
        CampaignDetailsResponse response = findCampaignService.findCampaignDetails(useCase);
        return APIResponseEntity.<CampaignDetailsResponse>builder()
            .message("캠페인 상세 페이지 상단 조회 성공")
            .data(response)
            .build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("/{campaignId}/percentage/category")
    public APIResponseEntity<PercentageResponse> findCampaignCategoryPercentages(
        @RequestParam("currency") String currencyType,
        @PathVariable Long campaignId,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        FindPercentangeUseCase useCase = new FindPercentangeUseCase(campaignId, userDetails.getId(), currencyType);
        PercentageResponse response = findPercentageService.findCategoryPercentage(useCase);
        return APIResponseEntity.<PercentageResponse>builder()
            .message("캠페인 카테고리별 비율 조회 성공")
            .data(response)
            .build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("/{campaignId}/percentage/date")
    public APIResponseEntity<PercentageResponse> findCampaignDatePercentages(
        @RequestParam("currency") String currencyType,
        @PathVariable Long campaignId,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        FindPercentangeUseCase useCase = new FindPercentangeUseCase(campaignId, userDetails.getId(), currencyType);
        PercentageResponse response = findPercentageService.findDatePercentage(useCase);
        response.sortPercentages();
        return APIResponseEntity.<PercentageResponse>builder()
            .message("캠페인 날짜별 비율 조회 성공")
            .data(response)
            .build();
    }
}
