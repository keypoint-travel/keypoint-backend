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
import lombok.Builder;
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
    private LocalDateTime startedAt;

    @Column(nullable = false)
    private LocalDateTime expirationAt;

    @Column(nullable = false)
    private boolean isActive;

    @Column(nullable = false)
    private boolean isFree;

    @Builder
    public MemberPremium(Member member, LocalDateTime expirationAt, boolean isActive,
        boolean isFree) {
        this.member = member;
        this.startedAt = LocalDateTime.now();
        this.expirationAt = expirationAt;
        this.isActive = isActive;
        this.isFree = isFree;
    }

    public void updateExpirationAt(LocalDateTime expirationAt) {
        this.expirationAt = expirationAt;
    }

    public void updateIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public void updateIsFree(boolean isFree) {
        this.isFree = isFree;
    }

    public void updateStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }
}
