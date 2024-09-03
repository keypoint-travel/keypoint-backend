package com.keypoint.keypointtravel.receipt.redis.entity;

import com.keypoint.keypointtravel.global.enumType.ocr.CurrencyCode;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Builder
@AllArgsConstructor
@RedisHash(value = "tempReceipt", timeToLive = 86400L) // 24시간
public class TempReceipt {
    @Id
    private String id;

    private String receiptType;

    private String merchantPhoneNumber;

    private CurrencyCode currencyCode;

    private Float subtotal;

    private Float tax;

    private Float tip;
}
