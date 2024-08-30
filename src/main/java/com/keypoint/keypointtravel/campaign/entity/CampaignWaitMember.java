package com.keypoint.keypointtravel.campaign.entity;

import com.keypoint.keypointtravel.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "campaign_wait_member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CampaignWaitMember {

    @Id
    @Column(name="campaign_wait_member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public CampaignWaitMember(Campaign campaign, Member member) {
        this.campaign = campaign;
        this.member = member;
    }
}
