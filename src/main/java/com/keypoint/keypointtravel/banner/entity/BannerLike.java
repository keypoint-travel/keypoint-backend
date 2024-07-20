package com.keypoint.keypointtravel.banner.entity;


import com.keypoint.keypointtravel.global.entity.BaseEntity;
import com.keypoint.keypointtravel.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name="banner_like")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BannerLike extends BaseEntity {

    @Id
    @Column(name = "banner_like_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "banner_id")
    private Banner banner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public BannerLike(Banner banner, Member member) {
        this.banner = banner;
        this.member = member;
    }
}
