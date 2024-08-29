package com.keypoint.keypointtravel.campaign.dto.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReceiptInfoDto {

    private Long receiptId;
    private String receiptImage;
    private LocalDateTime paidAt;
    private double latitude;
    private double longitude;
}
