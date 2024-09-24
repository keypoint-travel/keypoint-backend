package com.keypoint.keypointtravel.receipt.dto.useCase.updateReceiptUseCase;

import com.keypoint.keypointtravel.global.enumType.receipt.ReceiptCategory;
import com.keypoint.keypointtravel.receipt.dto.request.updateReceiptRequest.UpdateReceiptRequest;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UpdateReceiptUseCase {
    private Long receiptId;
    private String store;
    private String storeAddress;
    private ReceiptCategory receiptCategory;
    private LocalDateTime paidAt;
    private float totalAccount;
    private String memo;
    private List<UpdatePaymentItemUseCase> paymentItems;
    private Double longitude;
    private Double latitude;

    public static UpdateReceiptUseCase of(
            Long receiptId,
            UpdateReceiptRequest request
    ) {
        return UpdateReceiptUseCase.builder()
                .receiptId(receiptId)
                .store(request.getStore())
                .storeAddress(request.getStoreAddress())
                .receiptCategory(ReceiptCategory.getConstant(request.getReceiptCategory()))
                .paidAt(request.getPaidAt())
            .totalAccount(request.getTotalAmount())
                .memo(request.getMemo())
                .paymentItems(
                        request.getPaymentItems().stream().map(UpdatePaymentItemUseCase::new).toList())
                .longitude(request.getLongitude())
                .latitude(request.getLatitude())
                .build();
    }
}
