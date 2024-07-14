package com.keypoint.keypointtravel.banner.entity;


import com.keypoint.keypointtravel.global.entity.BaseEntity;
import com.keypoint.keypointtravel.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "banner_comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BannerComment extends BaseEntity {

    @Id
    @Column(name="banner_comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "banner_id")
    private Banner banner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String content;

    private boolean isDeleted;

    public BannerComment(Banner banner, Member member, String content) {
        this.banner = banner;
        this.member = member;
        this.content = content;
        this.isDeleted = false;
    }
}
