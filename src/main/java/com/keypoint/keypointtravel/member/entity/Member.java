package com.keypoint.keypointtravel.member.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.Comment;

import com.keypoint.keypointtravel.global.converter.AES256ToStringConverter;
import com.keypoint.keypointtravel.global.entity.BaseEntity;
import com.keypoint.keypointtravel.global.enumType.member.OauthProviderType;
import com.keypoint.keypointtravel.global.enumType.member.RoleType;
import com.keypoint.keypointtravel.notification.entity.Notification;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Convert(converter = AES256ToStringConverter.class)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleType role;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OauthProviderType oauthProviderType;

    @Column
    @Comment("최근 비밀번호 변경")
    private LocalDateTime lastPasswordUpdatedAt;

    @Column
    @Comment("최근 로그인 일자")
    private LocalDateTime recentLoginAt;

    @Column(nullable = false, name = "is_deleted")
    @Comment("삭제 여부")
    private boolean isDeleted;

    @OneToOne(mappedBy = "member", cascade = CascadeType.REMOVE)
    private MemberDetail memberDetail;

    @OneToOne(mappedBy = "member", cascade = CascadeType.REMOVE)
    private MemberConsent memberConsent;

    @OneToOne(mappedBy = "member", cascade = CascadeType.REMOVE)
    private Notification notification;

    public Member(String email, OauthProviderType oauthProviderType) {
        this.email = email;
        this.oauthProviderType = oauthProviderType;
        this.isDeleted = false;
        this.role = RoleType.ROLE_CERTIFIED_USER;
    }

    public Member(String email, String password, OauthProviderType oauthProviderType) {
        this.email = email;
        this.password = password;
        this.oauthProviderType = oauthProviderType;
        this.isDeleted = false;
        this.role = RoleType.ROLE_CERTIFIED_USER;
        this.lastPasswordUpdatedAt = LocalDateTime.now();
    }
}
