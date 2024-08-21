package com.keypoint.keypointtravel.receipt.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Comment;
import org.joda.time.LocalDateTime;

import com.keypoint.keypointtravel.campaign.entity.Campaign;
import com.keypoint.keypointtravel.global.entity.BaseEntity;
import com.keypoint.keypointtravel.global.enumType.currency.CurrencyType;
import com.keypoint.keypointtravel.global.enumType.receipt.ReceiptCategory;
import com.keypoint.keypointtravel.global.enumType.receipt.ReceiptRegistrationType;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "receipt")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Receipt extends BaseEntity {

    @Id
    @Column(name = "receipt_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;

    @OneToMany(mappedBy = "receipt", orphanRemoval = true)
    private List<PaymentItem> paymentItems = new ArrayList<>();

    @Comment("매장 이름")
    @Column(nullable = false)
    private String store;

    @Comment("매장 주소")
    private String storeAddress;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReceiptCategory receiptCategory;

    @Column(nullable = false)
    private float totalAmount;

    private String memo;

    @Column(nullable = false)
    private LocalDateTime paidAt;

    @Column(nullable = false)
    private Long receiptImageId;

    @Comment("경도")
    private Double longitude;

    @Comment("위도")
    private Double latitude;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReceiptRegistrationType receiptRegistrationType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CurrencyType currency;
}
