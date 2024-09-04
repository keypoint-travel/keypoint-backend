package com.keypoint.keypointtravel.receipt.dto.dto;

import lombok.Getter;

@Getter
public class PaymentMemberDTO {

    private Long id;
    private Long memberId;

    public PaymentMemberDTO(Long id, Long memberId) {
        this.id = id;
        this.memberId = memberId;
    }
}
