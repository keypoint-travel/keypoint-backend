package com.keypoint.keypointtravel.receipt.dto.response.receiptOCRResult;

import com.keypoint.keypointtravel.external.azure.dto.useCase.WholeReceiptUseCase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ReceiptOCRResponse {

    private String receiptId;
    private String receiptImageUrl;
    private String store;
    private String storeAddress;
    private LocalDateTime paidAt;
    private Float totalAccount;
    private List<PaymentItemOCRResponse> paymentItems;

    public static ReceiptOCRResponse from(String receiptImageUrl, WholeReceiptUseCase dto) {
        return ReceiptOCRResponse.builder()
            .receiptImageUrl(receiptImageUrl)
            .store(dto.getMerchantName())
            .storeAddress(dto.getMerchantAddress())
            .paidAt(ReceiptOCRResponse.getPaidAt(dto))
            .totalAccount(dto.getTotal())
            .paymentItems(dto.getItems().stream().map(PaymentItemOCRResponse::new).toList())
            .build();
    }

    private static LocalDateTime getPaidAt(WholeReceiptUseCase dto) {
        try {
            LocalDate date = LocalDate.parse(dto.getTransactionDate());
            LocalTime time = LocalTime.parse(dto.getTransactionTime());

            return LocalDateTime.of(date, time);
        } catch (Exception ex) {
            return null;
        }
    }
}
