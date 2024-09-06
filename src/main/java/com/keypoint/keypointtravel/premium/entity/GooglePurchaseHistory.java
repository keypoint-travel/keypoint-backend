package com.keypoint.keypointtravel.premium.entity;

import com.keypoint.keypointtravel.global.enumType.currency.CurrencyType;
import com.keypoint.keypointtravel.global.enumType.premium.PurchaseStatus;
import com.keypoint.keypointtravel.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Getter
@Table(name = "google_purchase_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GooglePurchaseHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "google_purchase_history_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private String orderId;

    private String productId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String purchaseToken;

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
    public GooglePurchaseHistory(Member member, String orderId, String productId, String purchaseToken, float amount,
                                 CurrencyType currency, Date purchasedAt, PurchaseStatus purchaseStatus) {
        this.member = member;
        this.orderId = orderId;
        this.productId = productId;
        this.purchaseToken = purchaseToken;
        this.amount = amount;
        this.currency = currency;
        this.purchasedAt = purchasedAt;
        this.purchaseStatus = purchaseStatus;
    }
}
