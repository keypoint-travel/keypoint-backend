package com.keypoint.keypointtravel.receipt.dto.request.updateReceiptRequest;

import com.google.firebase.database.annotations.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.List;

@Getter
public class UpdatePaymentItemRequest {

    private Long paymentItemId;

    @NotBlank
    @Size(max = 50) // TODO 길이 확인 후 변경
    private String itemName;

    @Min(1)
    private float amount;

    @Min(1)
    private long quantity;

    @NotNull
    private List<Long> memberIds;
}
