package com.keypoint.keypointtravel.banner.repository.bannerComment;

import com.keypoint.keypointtravel.banner.entity.BannerComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerCommentRepository extends JpaRepository<BannerComment, Long>, CommentCustomRepository {
}
