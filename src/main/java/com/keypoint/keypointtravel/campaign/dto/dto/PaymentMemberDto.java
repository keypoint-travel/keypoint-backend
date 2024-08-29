package com.keypoint.keypointtravel.campaign.dto.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentMemberDto {
    private final Long paymentItemId;
    private final Long memberId;
    private final String memberName;
}
