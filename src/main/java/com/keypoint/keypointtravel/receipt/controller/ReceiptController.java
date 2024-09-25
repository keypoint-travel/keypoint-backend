package com.keypoint.keypointtravel.receipt.controller;

import com.keypoint.keypointtravel.external.azure.service.AzureOCRService;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.global.utils.LogUtils;
import com.keypoint.keypointtravel.receipt.dto.request.updateReceiptRequest.UpdateReceiptRequest;
import com.keypoint.keypointtravel.receipt.dto.response.receiptOCRResult.ReceiptOCRResponse;
import com.keypoint.keypointtravel.receipt.dto.response.receiptResponse.ReceiptResponse;
import com.keypoint.keypointtravel.receipt.dto.useCase.ReceiptIdUseCase;
import com.keypoint.keypointtravel.receipt.dto.useCase.ReceiptImageUseCase;
import com.keypoint.keypointtravel.receipt.dto.useCase.updateReceiptUseCase.UpdateReceiptUseCase;
import com.keypoint.keypointtravel.receipt.service.MobileReceiptService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/receipts")
public class ReceiptController {

    private final AzureOCRService azureOCRService;

    private final MobileReceiptService mobileReceiptService;

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PostMapping("/analyze")
    public APIResponseEntity<ReceiptOCRResponse> analyzeReceipt(
        @RequestPart(value = "receiptImage", required = false) MultipartFile receiptImage
    ) {
        LogUtils.writeInfoLog("analyzeReceipt",
            "/api/v1/receipts/analyze"
        );
        ReceiptImageUseCase useCase = ReceiptImageUseCase.from(receiptImage);
        ReceiptOCRResponse result = azureOCRService.analyzeReceipt(useCase);

        return APIResponseEntity.<ReceiptOCRResponse>builder()
            .message("OCR 결과 요청 성공")
            .data(result)
            .build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("/{receiptId}")
    public APIResponseEntity<ReceiptResponse> getReceipt(
        @PathVariable(value = "receiptId") Long receiptId
    ) {
        ReceiptIdUseCase useCase = ReceiptIdUseCase.from(receiptId);
        ReceiptResponse response = mobileReceiptService.getReceipt(useCase);

        return APIResponseEntity.<ReceiptResponse>builder()
            .message("영수증 조회 성공")
            .data(response)
            .build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @DeleteMapping("/{receiptId}")
    public APIResponseEntity<Void> deleteReceipt(
        @PathVariable(value = "receiptId") Long receiptId
    ) {
        ReceiptIdUseCase useCase = ReceiptIdUseCase.from(receiptId);
        mobileReceiptService.deleteReceipt(useCase);

        return APIResponseEntity.<Void>builder()
            .message("영수증 삭제 성공")
            .build();
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PutMapping("/{receiptId}")
    public APIResponseEntity<Void> updateReceipt(
            @PathVariable(value = "receiptId") Long receiptId,
            @Valid @RequestBody UpdateReceiptRequest request
    ) {
        UpdateReceiptUseCase useCase = UpdateReceiptUseCase.of(receiptId, request);
        mobileReceiptService.updateReceipt(useCase);

        return APIResponseEntity.<Void>builder()
                .message("영수증 수정 성공")
                .build();
    }
}
