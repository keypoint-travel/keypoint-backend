package com.keypoint.keypointtravel.campaign.dto.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PaymentDto {

    private List<PaymentInfo> paymentList;
    private Long totalCount;
}
