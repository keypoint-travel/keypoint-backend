package com.keypoint.keypointtravel.banner.entity;

import com.keypoint.keypointtravel.global.entity.BaseEntity;
import com.keypoint.keypointtravel.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "banner_comment_like")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BannerCommentLike extends BaseEntity {

    @Id
    @Column(name = "banner_comment_like_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "banner_comment_id")
    private BannerComment bannerComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public BannerCommentLike(BannerComment bannerComment, Member member) {
        this.bannerComment = bannerComment;
        this.member = member;
    }
}
