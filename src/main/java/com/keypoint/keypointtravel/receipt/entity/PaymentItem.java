package com.keypoint.keypointtravel.receipt.entity;

import org.springframework.data.annotation.Id;

import com.keypoint.keypointtravel.global.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Entity
@Getter
@Table(name = "payment_item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentItem extends BaseEntity{
    @Id
    @Column(name="payment_item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receipt_id")
    private Receipt receipt;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private long quantity;

    @Column(nullable = false)
    private long amount;
}
