package com.keypoint.keypointtravel.premium.dto.apple;

import com.keypoint.keypointtravel.global.enumType.premium.PurchaseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class UserReceiptInfo {

    // TODO - User(구매자) 정보에 대한 Fields 필요.

    private Long appItemId;
    private String productId;
    private String transactionId;
    private String originalTransactionId;
    private Date purchaseDateMs;
    private Date expirationDate;
    private PurchaseStatus purchaseStatus;

}
