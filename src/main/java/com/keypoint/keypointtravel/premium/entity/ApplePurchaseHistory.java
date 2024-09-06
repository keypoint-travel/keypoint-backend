package com.keypoint.keypointtravel.premium.entity;

import com.keypoint.keypointtravel.global.enumType.currency.CurrencyType;
import com.keypoint.keypointtravel.global.enumType.premium.PurchaseStatus;
import com.keypoint.keypointtravel.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Date;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "apple_purchase_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApplePurchaseHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "apple_purchase_history_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private String productId;

    @Column(nullable = false)
    private String transactionId;

    @Column(nullable = false)
    private String originalTransactionId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String receiptData;

    @Column(nullable = false)
    private float amount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CurrencyType currency;

    @Column(nullable = false)
    private Date purchasedAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PurchaseStatus purchaseStatus;

    @Builder
    public ApplePurchaseHistory(Member member, String productId, String transactionId, String receiptData,
        String originalTransactionId, float amount, CurrencyType currency, Date purchasedAt,
        PurchaseStatus purchaseStatus) {
        this.member = member;
        this.productId = productId;
        this.transactionId = transactionId;
        this.receiptData = receiptData;
        this.originalTransactionId = originalTransactionId;
        this.amount = amount;
        this.currency = currency;
        this.purchasedAt = purchasedAt;
        this.purchaseStatus = purchaseStatus;
    }
}
