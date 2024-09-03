package com.keypoint.keypointtravel.receipt.dto.response.receiptResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PaymentMemberResponse {

    private Long memberId;

    public PaymentMemberResponse(Long memberId) {
        this.memberId = memberId;
    }
}
