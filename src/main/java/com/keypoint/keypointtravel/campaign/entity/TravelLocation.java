package com.keypoint.keypointtravel.campaign.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "travel_location")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TravelLocation {

    @Id
    @Column(name = "travel_location_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;

    @Column(nullable = false)
    private Long placeId;

    @Column(nullable = false)
    private Long sequence;

    public TravelLocation(Campaign campaign, Long placeId, Long sequence) {
        this.campaign = campaign;
        this.placeId = placeId;
        this.sequence = sequence;
    }
}
