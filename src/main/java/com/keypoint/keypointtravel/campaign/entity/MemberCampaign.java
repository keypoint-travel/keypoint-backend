package com.keypoint.keypointtravel.campaign.entity;

import com.keypoint.keypointtravel.global.entity.BaseEntity;
import com.keypoint.keypointtravel.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "member_campaign")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberCampaign extends BaseEntity {

    @Id
    @Column(name="member_campaign_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private boolean isLeader;

    public MemberCampaign(Campaign campaign, Member member, boolean isLeader) {
        this.campaign = campaign;
        this.member = member;
        this.isLeader = isLeader;
    }
}
