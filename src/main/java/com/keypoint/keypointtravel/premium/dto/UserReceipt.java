package com.keypoint.keypointtravel.premium.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserReceipt {

    // TODO - User(구매자) 정보에 대한 Fields 필요.

    @JsonAlias("receipt-data")
    private String receiptData;

    public void updateReceiptData(String receiptData) {
        this.receiptData = receiptData;
    }
}
