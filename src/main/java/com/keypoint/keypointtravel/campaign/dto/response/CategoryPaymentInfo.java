package com.keypoint.keypointtravel.campaign.dto.response;

import com.keypoint.keypointtravel.campaign.dto.dto.PaymentDto;
import com.keypoint.keypointtravel.campaign.dto.dto.PaymentMemberDto;
import com.keypoint.keypointtravel.global.enumType.receipt.ReceiptCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class CategoryPaymentInfo implements Comparable<CategoryPaymentInfo> {
    private Long paymentItemId;
    private ReceiptCategory category;
    private String storeName;
    private LocalDateTime paidAt;
    private List<ParticipationMemberInfo> members;
    private float amount;
    private Long receiptId;

    public static CategoryPaymentInfo from(PaymentDto dto) {
        return CategoryPaymentInfo.builder()
            .paymentItemId(dto.getPaymentItemId())
            .category(dto.getCategory())
            .storeName(dto.getStore())
            .paidAt(dto.getPaidAt())
            .members(new ArrayList<>())
            .amount(Math.round(dto.getAmount() * dto.getQuantity() * 100) / 100f)
            .receiptId(dto.getReceiptId())
            .build();
    }

    public void addMembers(List<PaymentMemberDto> dtoList) {
        for (PaymentMemberDto dto : dtoList) {
            if (dto.getPaymentItemId().equals(this.paymentItemId)) {
                members.add(new ParticipationMemberInfo(dto.getMemberId(), dto.getMemberName()));
            }
        }
    }

    @Override
    public int compareTo(CategoryPaymentInfo categoryPaymentInfo) {
        if (this.category.getCode() == categoryPaymentInfo.category.getCode()) {
            return categoryPaymentInfo.getPaidAt().compareTo(this.getPaidAt());
        }
        return this.category.getCode() - categoryPaymentInfo.category.getCode();
    }
}
