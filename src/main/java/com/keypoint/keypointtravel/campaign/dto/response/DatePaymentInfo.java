package com.keypoint.keypointtravel.campaign.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class DatePaymentInfo implements Comparable<DatePaymentInfo> {
    private Long paymentItemId;
    private String storeName;
    private String date;
    private LocalDateTime paidAt;
    private List<ParticipationMemberInfo> members;
    private float amount;
    private Long receiptId;

    public static DatePaymentInfo from(CategoryPaymentInfo info) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy.MM.dd");
        return DatePaymentInfo.builder()
            .paymentItemId(info.getPaymentItemId())
            .storeName(info.getStoreName())
            .date(info.getPaidAt().format(formatter))
            .paidAt(info.getPaidAt())
            .members(info.getMembers())
            .amount(info.getAmount())
            .receiptId(info.getReceiptId())
            .build();
    }

    @Override
    public int compareTo(DatePaymentInfo datePaymentInfo) {
        if (this.getDate().equals(datePaymentInfo.getDate())) {
            return datePaymentInfo.getPaidAt().compareTo(this.getPaidAt());
        }
        return datePaymentInfo.getDate().compareTo(this.getDate());
    }
}
