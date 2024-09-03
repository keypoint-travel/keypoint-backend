package com.keypoint.keypointtravel.receipt.controller;

import com.keypoint.keypointtravel.external.azure.service.AzureOCRService;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.global.enumType.receipt.ReceiptRegistrationType;
import com.keypoint.keypointtravel.receipt.dto.request.createReceiptRequest.CreateReceiptRequest;
import com.keypoint.keypointtravel.receipt.dto.response.receiptOCRResult.ReceiptOCRResponse;
import com.keypoint.keypointtravel.receipt.dto.useCase.ReceiptImageUseCase;
import com.keypoint.keypointtravel.receipt.dto.useCase.createReceiptUseCase.CreateReceiptUseCase;
import com.keypoint.keypointtravel.receipt.service.CreateReceiptService;
import jakarta.validation.Valid;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/receipts")
public class ReceiptController {

    private final AzureOCRService azureOCRService;

    private final CreateReceiptService createReceiptService;

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("/analyze")
    public APIResponseEntity<ReceiptOCRResponse> analyzeReceipt(
        @RequestPart(value = "receiptImage", required = false) MultipartFile receiptImage
    ) {
        ReceiptImageUseCase useCase = ReceiptImageUseCase.from(receiptImage);
        ReceiptOCRResponse result = azureOCRService.analyzeReceipt(useCase);

        return APIResponseEntity.<ReceiptOCRResponse>builder()
            .message(" OCR 결과 요청 성공")
            .data(result)
            .build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PostMapping("/{campaignId}")
    public APIResponseEntity<Void> addReceipt(
        @PathVariable(value = "campaignId") Long campaignId,
        @RequestParam(value = "receipt-registration-type") ReceiptRegistrationType registrationType,
        @Valid @RequestBody CreateReceiptRequest request
    ) throws IOException {
        CreateReceiptUseCase useCase = CreateReceiptUseCase.of(campaignId, request, registrationType);
        createReceiptService.addReceipt(useCase);

        return APIResponseEntity.<Void>builder()
            .message("영수증 등록")
            .build();
    }
}
