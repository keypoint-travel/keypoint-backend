package com.keypoint.keypointtravel.receipt.dto.useCase.createReceiptUseCase;

import com.keypoint.keypointtravel.campaign.entity.Campaign;
import com.keypoint.keypointtravel.global.enumType.currency.CurrencyType;
import com.keypoint.keypointtravel.global.enumType.receipt.ReceiptCategory;
import com.keypoint.keypointtravel.global.enumType.receipt.ReceiptRegistrationType;
import com.keypoint.keypointtravel.receipt.dto.request.createReceiptRequest.CreateReceiptRequest;
import com.keypoint.keypointtravel.receipt.entity.Receipt;
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
    private String memo;
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
            .memo(request.getMemo())
            .paymentItems(
                request.getPaymentItems().stream().map(CreatePaymentItemUseCase::new).toList())
            .longitude(request.getLongitude())
            .latitude(request.getLatitude())
            .receiptImageUrl(request.getReceiptImageUrl())
            .build();
    }

    public Receipt toEntity(
        Campaign campaign,
        Long receiptImageId,
        CurrencyType currency
    ) {
        return Receipt.builder()
            .receiptRegistrationType(registrationType)
            .campaign(campaign)
            .store(this.store)
            .storeAddress(this.storeAddress)
            .receiptCategory(this.receiptCategory)
            .totalAmount(this.totalAccount)
            .memo(this.memo)
            .paidAt(this.paidAt)
            .receiptImageId(receiptImageId)
            .longitude(this.longitude)
            .latitude(this.latitude)
            .currency(currency)
            .build();
    }
}
