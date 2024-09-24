package com.keypoint.keypointtravel.receipt.entity;

import com.keypoint.keypointtravel.campaign.entity.Campaign;
import com.keypoint.keypointtravel.global.entity.BaseEntity;
import com.keypoint.keypointtravel.global.enumType.currency.CurrencyType;
import com.keypoint.keypointtravel.global.enumType.receipt.ReceiptCategory;
import com.keypoint.keypointtravel.global.enumType.receipt.ReceiptRegistrationType;
import com.keypoint.keypointtravel.receipt.redis.entity.TempReceipt;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

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

    @Column()
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

    private String receiptType;

    private String merchantPhoneNumber;

    private Float subtotal;

    private Float tax;

    private Float tip;

    @Column(nullable = false, name = "is_deleted")
    @ColumnDefault("false")
    @Comment("삭제 여부")
    private boolean isDeleted;

    @Builder
    public Receipt(
        ReceiptRegistrationType receiptRegistrationType,
        Campaign campaign,
        String store,
        String storeAddress,
        ReceiptCategory receiptCategory,
        float totalAmount,
        String memo,
        LocalDateTime paidAt,
        Long receiptImageId,
        Double longitude,
        Double latitude,
        CurrencyType currency,
        TempReceipt tempReceipt
    ) {
        this.receiptRegistrationType = receiptRegistrationType;
        this.campaign = campaign;
        this.store = store;
        this.storeAddress = storeAddress;
        this.receiptCategory = receiptCategory;
        this.totalAmount = totalAmount;
        this.memo = memo;
        this.paidAt = paidAt;
        this.receiptImageId = receiptImageId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.currency = currency;

        if (tempReceipt != null) {
            this.receiptType = tempReceipt.getReceiptType();
            this.merchantPhoneNumber = tempReceipt.getMerchantPhoneNumber();
            this.subtotal = tempReceipt.getSubtotal();
            this.tax = tempReceipt.getTax();
            this.tip = tempReceipt.getTip();
        }
    }
}
