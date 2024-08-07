package com.keypoint.keypointtravel.campaign.entity;

import com.keypoint.keypointtravel.global.entity.BaseEntity;
import com.keypoint.keypointtravel.global.enumType.campaign.Status;
import com.keypoint.keypointtravel.global.enumType.campaign.TravelType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "campaign")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Campaign extends BaseEntity {

    @Id
    @Column(name = "campaign_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TravelType travelType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    private String campaignImageId;

    public Campaign(String title, String campaignImageId, TravelType travelType) {
        this.title = title;
        this.campaignImageId = campaignImageId;
        this.travelType = travelType;
        this.status = Status.IN_PROGRESS;
    }
}
