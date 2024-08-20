package com.keypoint.keypointtravel.receipt.dto.request.createReceiptRequest;

import com.google.firebase.database.annotations.NotNull;
import com.keypoint.keypointtravel.global.annotation.ValidEnum;
import com.keypoint.keypointtravel.global.enumType.receipt.ReceiptCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;

@Getter
public class CreateReceiptRequest {

    @NotNull
    private Long campaignId;

    @NotBlank
    @Size(max = 50) // TODO 길이 확인 후 변경
    private String store;

    private String storeAddress;

    @ValidEnum(enumClass = ReceiptCategory.class)
    private ReceiptCategory receiptCategory;

    @NotNull
    private LocalDateTime paidAt;

    @Min(1)
    private float totalAccount;

    @NotNull
    private List<CreatePaymentItemRequest> paymentItems;

    private Double longitude;

    private Double latitude;
}
