package com.keypoint.keypointtravel.campaign.entity;

import com.keypoint.keypointtravel.global.enumType.currency.CurrencyType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "campaign_budget")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CampaignBudget {

    @Id
    @Column(name = "campaign_budget_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CurrencyType currency;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private Long amount;

    @Builder
    public CampaignBudget(Campaign campaign, CurrencyType currency, String category, Long amount) {
        this.campaign = campaign;
        this.currency = currency;
        this.category = category;
        this.amount = amount;
    }
}
