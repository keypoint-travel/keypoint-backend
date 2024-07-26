package com.keypoint.keypointtravel.blocked_member.entity;

import com.keypoint.keypointtravel.global.entity.BaseEntity;
import com.keypoint.keypointtravel.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "blocked_member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BlockedMember extends BaseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long blockedMemberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public BlockedMember(Long blockedMemberId, Member member) {
        this.blockedMemberId = blockedMemberId;
        this.member = member;
    }
}
