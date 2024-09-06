package com.keypoint.keypointtravel.premium.controller;

import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.premium.dto.request.AppleReceiptRequest;
import com.keypoint.keypointtravel.premium.dto.useCase.ApplePurchaseUseCase;
import com.keypoint.keypointtravel.premium.service.AppleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/premiums/apple")
@RequiredArgsConstructor
public class AppleController {

    private final AppleService appleService;

    // 결제 영수증 검증 및 상품 구매 완료 응답
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PostMapping
    public APIResponseEntity<Void> applePurchase(
        @RequestBody @Valid AppleReceiptRequest appleReceiptRequest,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        ApplePurchaseUseCase purchaseUseCase = new ApplePurchaseUseCase(
            userDetails.getId(),
            appleReceiptRequest.getAmount(),
            appleReceiptRequest.getCurrency(),
            appleReceiptRequest.getReceiptData());
        appleService.updatePurchaseHistory(purchaseUseCase);
        return APIResponseEntity.<Void>builder()
            .message("애플 결제 확인 및 프리미엄 적용 성공")
            .build();
    }
}
