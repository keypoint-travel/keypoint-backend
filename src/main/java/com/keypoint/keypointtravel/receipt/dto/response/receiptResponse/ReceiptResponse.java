package com.keypoint.keypointtravel.receipt.dto.response.receiptResponse;

import com.keypoint.keypointtravel.global.enumType.currency.CurrencyType;
import com.keypoint.keypointtravel.global.enumType.receipt.ReceiptCategory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReceiptResponse {

    private LocalDateTime paidAt;
    private Long receiptId;
    private String store;
    private String storeAddress;
    private ReceiptCategory receiptCategory;
    private List<PaymentItemResponse> paymentItems;
    private String receiptImageUrl;
    private Float totalAmount;
    private CurrencyType currencyType;
    private String memo;

    public ReceiptResponse(
        LocalDateTime paidAt,
        Long receiptId,
        String store,
        String storeAddress,
        ReceiptCategory receiptCategory,
        String receiptImageUrl,
        Float totalAccount,
        CurrencyType currencyType,
        String memo
    ) {
        this.paidAt = paidAt;
        this.receiptId = receiptId;
        this.store = store;
        this.storeAddress = storeAddress;
        this.receiptCategory = receiptCategory;
        this.paymentItems = paymentItems;
        this.receiptImageUrl = receiptImageUrl;
        this.totalAmount = totalAccount;
        this.currencyType = currencyType;
        this.memo = memo;
    }

    public void setPaymentItems(List<PaymentItemResponse> paymentItems) {
        this.paymentItems = paymentItems;
    }
}
