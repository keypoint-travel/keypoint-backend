package com.keypoint.keypointtravel.campaign.dto.response.details;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReceiptInfo {

    private Long receiptId;
    private String receiptImage;
    private int sequence;
    private double latitude;
    private double longitude;
}
