package com.keypoint.keypointtravel.banner.repository.bannerLike;

import com.keypoint.keypointtravel.banner.entity.BannerLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerLikeRepository extends JpaRepository<BannerLike, Long>, LikeCustomRepository {
}
