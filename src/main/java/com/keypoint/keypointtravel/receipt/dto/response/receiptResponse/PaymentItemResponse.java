package com.keypoint.keypointtravel.receipt.dto.response.receiptResponse;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PaymentItemResponse {

    private Long paymentItemId;
    private String itemName;
    private long quantity;
    private float amount;
    private List<Long> memberIds;
}
