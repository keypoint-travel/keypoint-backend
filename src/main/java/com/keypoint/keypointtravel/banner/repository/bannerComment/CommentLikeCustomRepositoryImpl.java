package com.keypoint.keypointtravel.banner.repository.bannerComment;

import com.keypoint.keypointtravel.banner.entity.QBannerCommentLike;
import com.keypoint.keypointtravel.global.enumType.error.BannerErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommentLikeCustomRepositoryImpl implements CommentLikeCustomRepository {

    private final JPAQueryFactory queryFactory;

    private final QBannerCommentLike bannerCommentLike = QBannerCommentLike.bannerCommentLike;

    @Override
    public void deleteLike(Long commentId, Long memberId) {
        long count = queryFactory.delete(bannerCommentLike)
            .where(bannerCommentLike.bannerComment.id.eq(commentId), bannerCommentLike.member.id.eq(memberId))
            .execute();
        if(count < 1){
            throw new GeneralException(BannerErrorCode.NOT_EXISTED_LIKE);
        }
    }
}
