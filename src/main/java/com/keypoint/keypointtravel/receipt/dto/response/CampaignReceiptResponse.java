package com.keypoint.keypointtravel.receipt.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CampaignReceiptResponse {

    private Long receiptId;
    private LocalDateTime paidAt;
    private String receiptImageUrl;
    private Double longitude;
    private Double latitude;
    @JsonProperty("isRegisteredAddress")
    private Boolean isRegisteredAddress;

    public CampaignReceiptResponse(
        Long receiptId,
        LocalDateTime paidAt,
        String receiptImageUrl,
        Double longitude,
        Double latitude
    ) {
        this.receiptId = receiptId;
        this.paidAt = paidAt;
        this.receiptImageUrl = receiptImageUrl;
        this.longitude = longitude;
        this.latitude = latitude;
        this.isRegisteredAddress = longitude != null && latitude != null;
    }
}
