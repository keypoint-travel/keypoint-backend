package com.keypoint.keypointtravel.campaign.dto.response.category;

import com.keypoint.keypointtravel.campaign.dto.dto.PaymentMemberDto;
import com.keypoint.keypointtravel.campaign.dto.dto.category.PaymentByCategoryDto;
import com.keypoint.keypointtravel.campaign.dto.response.PaymentMemberInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class CategoryPaymentResponse {

    private Long paymentItemId;
    private String storeName;
    private LocalDateTime paidAt;
    private float amount;
    private Long receiptId;
    private List<PaymentMemberInfo> members;

    public static CategoryPaymentResponse of(PaymentByCategoryDto payment, List<PaymentMemberDto> paymentMemberList) {
        return new CategoryPaymentResponse(
            payment.getPaymentItemId(),
            payment.getStoreName(),
            payment.getPaidAt(),
            Math.round(payment.getAmount() * payment.getQuantity() * 100) / 100.0f,
            payment.getReceiptId(),
            PaymentMemberInfo.of(payment.getPaymentItemId(), paymentMemberList)
        );
    }
}
