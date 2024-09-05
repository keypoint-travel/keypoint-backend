package com.keypoint.keypointtravel.premium.entity;

import com.keypoint.keypointtravel.global.enumType.premium.PremiumTitle;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "premium")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Premium {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "premium_id")
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PremiumTitle title;

    @Column(name = "price_usd")
    private float priceUSD;

    @Column(name = "price_krw")
    private float priceKRW;

    @Column(name = "price_jpy")
    private float priceJPY;

    @Column(nullable = false)
    private int durationInMonths;

    @Builder

    public Premium(PremiumTitle title, float priceUSD, float priceKRW, float priceJPY,
        int durationInMonths) {
        this.title = title;
        this.priceUSD = priceUSD;
        this.priceKRW = priceKRW;
        this.priceJPY = priceJPY;
        this.durationInMonths = durationInMonths;
    }
}
