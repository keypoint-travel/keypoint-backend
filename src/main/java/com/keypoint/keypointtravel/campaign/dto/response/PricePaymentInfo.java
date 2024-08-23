package com.keypoint.keypointtravel.campaign.dto.response;


import com.keypoint.keypointtravel.campaign.dto.dto.PaymentDto;
import com.keypoint.keypointtravel.campaign.dto.dto.PaymentMemberDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class PricePaymentInfo implements Comparable<PricePaymentInfo> {

    private Long paymentItemId;
    private float amount;
    private String storeName;
    private LocalDateTime paidAt;
    private List<ParticipationMemberInfo> members;
    private Long receiptId;

    public static PricePaymentInfo from(PaymentDto dto) {
        return PricePaymentInfo.builder()
            .paymentItemId(dto.getPaymentItemId())
            .storeName(dto.getStore())
            .paidAt(dto.getPaidAt())
            .members(new ArrayList<>())
            .amount(Math.round(dto.getPrice() * 100) / 100f)
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
    public int compareTo(PricePaymentInfo info) {
        return this.getAmount() - info.getAmount() < 0 ? 1 : -1;
    }
}
