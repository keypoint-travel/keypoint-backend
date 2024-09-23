package com.keypoint.keypointtravel.receipt.dto.request.createReceiptRequest;

import com.google.firebase.database.annotations.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class CreateReceiptRequest {

    private String receiptId;

    @NotBlank
    @Size(max = 50) // TODO 길이 확인 후 변경
    private String store;

    private String storeAddress;

    private String receiptCategory;

    @NotNull
    private LocalDateTime paidAt;

    @Min(1)
    private float totalAccount;

    @NotNull
    private List<CreatePaymentItemRequest> paymentItems;

    private Double longitude;

    private Double latitude;

    private String receiptImageUrl;

    private String memo;
}
