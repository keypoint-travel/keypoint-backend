package com.keypoint.keypointtravel.member.entity;

import com.keypoint.keypointtravel.global.entity.BaseEntity;
import com.keypoint.keypointtravel.global.enumType.member.CountryCode;
import com.keypoint.keypointtravel.global.enumType.member.GenderType;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private GenderType gender;

    @Column(nullable = false)
    private LocalDate birth;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Comment("국적")
    private CountryCode country;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Comment("앱 설정 언어")
    private LanguageCode language;

    private Long profileImageId; // AttachFile과 논리적 FK

    public MemberDetail(
        Member member,
        GenderType gender,
        LocalDate birth,
        String name,
        LanguageCode language
    ) {
        this.member = member;
        this.gender = gender;
        this.birth = birth;
        this.name = name;
        this.language = language;

        this.country = CountryCode.NONE; // todo API 연동 이후 수정 필요
    }
}
