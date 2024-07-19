package com.keypoint.keypointtravel.member.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.Comment;

import com.keypoint.keypointtravel.global.entity.BaseEntity;

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
@Table(name = "member_consent")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Comment("회원 동의")
public class MemberConsent extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_consent_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    @Comment("개인정보 이용동의서 확인 여부")
    private boolean doesAgreePrivacyPolicy;

    @Column(nullable = false)
    @Comment("이용 약관 확인 여부")
    private boolean doesAgreeTermsOfUse;

    @Column(nullable = false)
    @Comment("개인정보 이용동의서 확인 여부")
    private LocalDateTime privacyPolicyConsentAt;

    @Column(nullable = false)
    @Comment("이용 약관 확인 날짜")
    private LocalDateTime termsOfUseConsentAt;
}
