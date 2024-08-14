package com.keypoint.keypointtravel.banner.repository.bannerComment;

import com.keypoint.keypointtravel.banner.entity.BannerCommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<BannerCommentLike, Long>, CommentLikeCustomRepository {

    boolean existsByBannerCommentIdAndMemberId(Long commentId, Long memberId);
}
