package com.keypoint.keypointtravel.campaign.entity;

import com.keypoint.keypointtravel.global.entity.BaseEntity;
import com.keypoint.keypointtravel.global.enumType.campaign.Status;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

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
    private Status status;

    private Long campaignImageId;

    @Column(nullable = false)
    private Date startDate;

    @Column(nullable = false)
    private Date endDate;

    private String invitation_code;

    @OneToMany(mappedBy = "campaign", orphanRemoval = true)
    private List<MemberCampaign> memberCampaigns = new ArrayList<>();

    @Builder
    public Campaign(String title, Status status, Long campaignImageId, Date startDate, Date endDate, String invitation_code) {
        this.title = title;
        this.status = status;
        this.campaignImageId = campaignImageId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.invitation_code = invitation_code;
    }

    public void addInvitationCode(String invitation_code) {
        this.invitation_code = invitation_code;
    }
}
