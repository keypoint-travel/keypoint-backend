package com.keypoint.keypointtravel.receipt.dto.useCase.createReceiptUseCase;

import com.keypoint.keypointtravel.global.enumType.receipt.ReceiptCategory;
import com.keypoint.keypointtravel.global.enumType.receipt.ReceiptRegistrationType;
import com.keypoint.keypointtravel.receipt.dto.request.createReceiptRequest.CreateReceiptRequest;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CreateReceiptUseCase {

    private ReceiptRegistrationType registrationType;
    private Long campaignId;
    private String store;
    private String storeAddress;
    private ReceiptCategory receiptCategory;
    private LocalDateTime paidAt;
    private float totalAccount;
    private List<CreatePaymentItemUseCase> paymentItems;
    private Double longitude;
    private Double latitude;
    private String receiptImageUrl;

    public static CreateReceiptUseCase of(
        CreateReceiptRequest request,
        ReceiptRegistrationType registrationType
    ) {
        return CreateReceiptUseCase.builder()
            .registrationType(registrationType)
            .campaignId(request.getCampaignId())
            .store(request.getStore())
            .storeAddress(request.getStoreAddress())
            .receiptCategory(request.getReceiptCategory())
            .paidAt(request.getPaidAt())
            .totalAccount(request.getTotalAccount())
            .paymentItems(
                request.getPaymentItems().stream().map(CreatePaymentItemUseCase::new).toList())
            .longitude(request.getLongitude())
            .latitude(request.getLatitude())
            .receiptImageUrl(request.getReceiptImageUrl())
            .build();
    }
}
