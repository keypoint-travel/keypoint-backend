package com.keypoint.keypointtravel.premium.controller;

import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.premium.dto.request.GoogleReceiptRequest;
import com.keypoint.keypointtravel.premium.dto.useCase.GooglePurchaseUseCase;
import com.keypoint.keypointtravel.premium.service.GoogleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("/api/v1/premiums/google")
@RequiredArgsConstructor
public class GoogleController {

    private final GoogleService googleService;

    // 결제 영수증 검증 및 상품 구매 완료 응답
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PostMapping
    public APIResponseEntity<Void> googlePurchase(
        @RequestBody @Valid GoogleReceiptRequest googleReceiptRequest,
        @AuthenticationPrincipal CustomUserDetails userDetails) throws GeneralSecurityException, IOException {
        GooglePurchaseUseCase useCase = new GooglePurchaseUseCase(
            userDetails.getId(),
            googleReceiptRequest.getPackageName(),
            googleReceiptRequest.getProductId(),
            googleReceiptRequest.getPurchaseToken(),
            googleReceiptRequest.getAmount(),
            googleReceiptRequest.getCurrency());
        googleService.updatePurchaseHistory(useCase);
        return APIResponseEntity.<Void>builder()
            .message("구글 결제 확인 및 프리미엄 적용 성공")
            .build();
    }
}
