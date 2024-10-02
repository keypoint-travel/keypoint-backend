package com.keypoint.keypointtravel.campaign.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "campaign_report")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CampaignReport {

    @Id
    @Column(name = "campaign_report_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;

    @Column(nullable = false)
    private Long reportImageId;

    public CampaignReport(Campaign campaign, Long reportImageId) {
        this.campaign = campaign;
        this.reportImageId = reportImageId;
    }
}
