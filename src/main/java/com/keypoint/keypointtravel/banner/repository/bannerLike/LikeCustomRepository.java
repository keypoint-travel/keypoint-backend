package com.keypoint.keypointtravel.banner.repository.bannerLike;

public interface LikeCustomRepository {
    void deleteLike(Long bannerId, Long memberId);
}
