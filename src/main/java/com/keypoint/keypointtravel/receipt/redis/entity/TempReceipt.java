package com.keypoint.keypointtravel.receipt.redis.entity;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@NoArgsConstructor
@RedisHash(value = "tempReceipt", timeToLive = 86400L) // 24시간
public class TempReceipt {

    @Id
    private String id;

    @Indexed
    private String receiptType;

    private String merchantPhoneNumber;

    private Float subtotal;

    private Float tax;

    private Float tip;

    public TempReceipt(
        String receiptType,
        String merchantPhoneNumber,
        Float subtotal,
        Float tax,
        Float tip
    ) {
        this.receiptType = receiptType;
        this.merchantPhoneNumber = merchantPhoneNumber;
        this.subtotal = subtotal;
        this.tax = tax;
        this.tip = tip;
    }
}
