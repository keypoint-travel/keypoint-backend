package com.keypoint.keypointtravel.member.entity;

import com.keypoint.keypointtravel.badge.entity.Badge;
import com.keypoint.keypointtravel.global.entity.BaseEntity;
import com.keypoint.keypointtravel.global.enumType.member.GenderType;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.theme.entity.PaidTheme;
import com.keypoint.keypointtravel.theme.entity.Theme;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Table(name = "member_detail")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_detail_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "representative_badge_id")
    private Badge representativeBadge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id")
    private Theme theme;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paid_theme_id")
    private PaidTheme paidTheme;

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private GenderType gender;

    @Column(nullable = true)
    private LocalDate birth;

    @Column(nullable = true)
    @Comment("국적")
    private String country;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Comment("앱 설정 언어")
    private LanguageCode language;

    private Long profileImageId; // AttachFile과 논리적 FK

    @Comment("최근 영수증 등록 시간")
    private LocalDateTime recentRegisterReceiptAt;

    public MemberDetail(
        Member member,
        GenderType gender,
        LocalDate birth,
        LanguageCode language,
        String country
    ) {
        this.member = member;
        this.gender = gender;
        this.birth = birth;
        this.language = language;
        this.country = country;
    }
}
