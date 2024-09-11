package com.keypoint.keypointtravel.badge.entity;

import com.keypoint.keypointtravel.global.entity.BaseEntity;
import com.keypoint.keypointtravel.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "earn_badge")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EarnedBadge extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "earned_Badge")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "badge_id")
    private Badge badge;

    public EarnedBadge(
            Member member,
            Badge badge
    ) {
        this.member = member;
        this.badge = badge;
    }

    public static EarnedBadge of(
            Member member,
            Badge badge
    ) {
        return new EarnedBadge(member, badge);
    }
}
