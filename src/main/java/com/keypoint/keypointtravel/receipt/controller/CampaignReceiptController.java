package com.keypoint.keypointtravel.receipt.controller;

import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.global.enumType.receipt.ReceiptRegistrationType;
import com.keypoint.keypointtravel.receipt.dto.request.createReceiptRequest.CreateReceiptRequest;
import com.keypoint.keypointtravel.receipt.dto.response.CampaignReceiptResponse;
import com.keypoint.keypointtravel.receipt.dto.useCase.CampaignIdUseCase;
import com.keypoint.keypointtravel.receipt.dto.useCase.createReceiptUseCase.CreateReceiptUseCase;
import com.keypoint.keypointtravel.receipt.service.MobileReceiptService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/campaigns")
@RequiredArgsConstructor
public class CampaignReceiptController {

    private final MobileReceiptService mobileReceiptService;

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PostMapping("/{campaignId}/receipts")
    public APIResponseEntity<Void> addReceipt(
        @PathVariable(value = "campaignId") Long campaignId,
        @RequestParam(value = "receipt-registration-type") ReceiptRegistrationType registrationType,
        @Valid @RequestBody CreateReceiptRequest request
    ) {
        CreateReceiptUseCase useCase = CreateReceiptUseCase.of(campaignId, request,
            registrationType);
        mobileReceiptService.addReceipt(useCase);

        return APIResponseEntity.<Void>builder()
            .message("영수증 등록")
            .build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("/{campaignId}/receipts")
    public APIResponseEntity<List<CampaignReceiptResponse>> getReceiptsAboutCampaign(
        @PathVariable(value = "campaignId") Long campaignId
    ) {
        CampaignIdUseCase useCase = CampaignIdUseCase.from(campaignId);
        List<CampaignReceiptResponse> response = mobileReceiptService.getReceiptsAboutCampaign(
            useCase);

        return APIResponseEntity.<List<CampaignReceiptResponse>>builder()
            .message("캠페인에 등록된 영수증 조회")
            .data(response)
            .build();
    }
}
