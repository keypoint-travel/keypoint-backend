package com.keypoint.keypointtravel.receipt.dto.request.createReceiptRequest;

import java.util.List;

import com.google.firebase.database.annotations.NotNull;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CreatePaymentItemRequest {
    @NotBlank
    @Size(max = 50) // TODO 길이 확인 후 변경
    private String itemName;

    @Min(1)
    private long amount;

    @Min(1)
    private long quantity;

    @NotNull
    private List<String> memberIds;
}
