package com.keypoint.keypointtravel.campaign.controller;

import com.keypoint.keypointtravel.campaign.dto.response.category.CategoryPercentageResponse;
import com.keypoint.keypointtravel.campaign.dto.response.DetailsByCategoryResponse;
import com.keypoint.keypointtravel.campaign.dto.response.DetailsByDateResponse;
import com.keypoint.keypointtravel.campaign.dto.response.DetailsByPriceResponse;
import com.keypoint.keypointtravel.campaign.dto.useCase.FindPaymentUseCase;
import com.keypoint.keypointtravel.campaign.service.FindPercentageService;
import com.keypoint.keypointtravel.campaign.service.ReadCampaignService;
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

    private final ReadCampaignService readCampaignService;

    private final FindPercentageService findPercentageService;

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("/{campaignId}/percentage/category")
    public APIResponseEntity<CategoryPercentageResponse> findCampaignCategoryPercentages(
        @RequestParam("currency") String currencyType,
        @PathVariable Long campaignId,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        FindPaymentUseCase useCase = new FindPaymentUseCase(campaignId, userDetails.getId(), currencyType);
        CategoryPercentageResponse response = findPercentageService.findCategoryPercentage(useCase);
        return APIResponseEntity.<CategoryPercentageResponse>builder()
            .message("캠페인 카테고리 별 비율 조회 성공")
            .data(response)
            .build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("/{campaignId}/category")
    public APIResponseEntity<DetailsByCategoryResponse> findCampaignCategoryList(
        @RequestParam("currency") String currencyType,
        @PathVariable Long campaignId,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        FindPaymentUseCase useCase = new FindPaymentUseCase(campaignId, userDetails.getId(), currencyType);
        DetailsByCategoryResponse response = readCampaignService.findByCategory(useCase);
        response.sortPayments();
        return APIResponseEntity.<DetailsByCategoryResponse>builder()
            .message("캠페인 카테고리 별 결제 내역 조회 성공")
            .data(response)
            .build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("/{campaignId}/date")
    public APIResponseEntity<DetailsByDateResponse> findCampaignDateList(
        @RequestParam("currency") String currencyType,
        @PathVariable Long campaignId,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        FindPaymentUseCase useCase = new FindPaymentUseCase(campaignId, userDetails.getId(), currencyType);
        DetailsByCategoryResponse res = readCampaignService.findByDate(useCase);
        DetailsByDateResponse response = DetailsByDateResponse.from(res);
        response.sortPayments();
        return APIResponseEntity.<DetailsByDateResponse>builder()
            .message("캠페인 날짜 별 결제 내역 조회 성공")
            .data(response)
            .build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("/{campaignId}/price")
    public APIResponseEntity<DetailsByPriceResponse> findCampaignPriceList(
        @RequestParam("currency") String currencyType,
        @PathVariable Long campaignId,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        FindPaymentUseCase useCase = new FindPaymentUseCase(campaignId, userDetails.getId(), currencyType);
        DetailsByPriceResponse response = readCampaignService.findByPrice(useCase);
        response.sortPayments();
        return APIResponseEntity.<DetailsByPriceResponse>builder()
            .message("캠페인 날짜 별 결제 내역 조회 성공")
            .data(response)
            .build();
    }
}
