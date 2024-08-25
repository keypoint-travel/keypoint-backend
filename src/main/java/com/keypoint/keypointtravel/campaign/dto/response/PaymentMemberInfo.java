package com.keypoint.keypointtravel.campaign.dto.response;

import com.keypoint.keypointtravel.campaign.dto.dto.PaymentMemberDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class PaymentMemberInfo {

    private final Long memberId;
    private final String memberName;

    public static List<PaymentMemberInfo> of(Long paymentItemId, List<PaymentMemberDto> paymentMemberList) {
        List<PaymentMemberInfo> members = new ArrayList<>();
        for (PaymentMemberDto paymentMember : paymentMemberList) {
            if (paymentMember.getPaymentItemId().equals(paymentItemId)) {
                members.add(new PaymentMemberInfo(paymentMember.getMemberId(), paymentMember.getMemberName()));
            }
        }
        return members;
    }
}
