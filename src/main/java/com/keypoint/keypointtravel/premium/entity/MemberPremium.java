package com.keypoint.keypointtravel.premium.entity;

import com.keypoint.keypointtravel.global.entity.BaseEntity;
import com.keypoint.keypointtravel.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "member_premium")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberPremium extends BaseEntity {

    @Id
    @Column(name = "member_premium_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private LocalDateTime expiration_at;

    @Column(nullable = false)
    private boolean is_active;

    @Column(nullable = false)
    private boolean is_free;

    public MemberPremium(Member member, LocalDateTime expiration_at, boolean is_active,
        boolean is_free) {
        this.member = member;
        this.expiration_at = expiration_at;
        this.is_active = is_active;
        this.is_free = is_free;
    }
}
