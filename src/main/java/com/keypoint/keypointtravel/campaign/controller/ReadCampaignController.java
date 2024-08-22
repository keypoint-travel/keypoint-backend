package com.keypoint.keypointtravel.campaign.controller;

import com.keypoint.keypointtravel.campaign.dto.response.DetailsByCategoryResponse;
import com.keypoint.keypointtravel.campaign.dto.useCase.FindPaymentUseCase;
import com.keypoint.keypointtravel.campaign.service.ReadCampaignService;
import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.global.enumType.currency.CurrencyType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/campaigns/details")
@RequiredArgsConstructor
public class ReadCampaignController {

    private final ReadCampaignService readCampaignService;

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("/{campaignId}/category")
    public APIResponseEntity<DetailsByCategoryResponse> findCampaignCategoryList(
        @RequestParam CurrencyType currencyType,
        @PathVariable Long campaignId,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        FindPaymentUseCase useCase = new FindPaymentUseCase(campaignId, userDetails.getId(), currencyType);
        DetailsByCategoryResponse response = readCampaignService.findByCategory(useCase);
        return APIResponseEntity.<DetailsByCategoryResponse>builder()
            .message("캠페인 카테고리 별 결제 내역 조회 성공")
            .data(response)
            .build();
    }
}
