package com.keypoint.keypointtravel.banner.repository.bannerComment;


public interface CommentLikeCustomRepository {

    void deleteLike(Long commentId, Long memberId);
}
