package com.keypoint.keypointtravel.global.enumType.premium;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PurchaseStatus {
    CANCELED,
    PAID,
    REFUNDED,
    COMPLETED
}
