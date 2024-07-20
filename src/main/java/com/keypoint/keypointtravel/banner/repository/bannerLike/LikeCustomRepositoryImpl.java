package com.keypoint.keypointtravel.banner.repository.bannerLike;

import com.keypoint.keypointtravel.banner.entity.QBannerLike;
import com.keypoint.keypointtravel.global.enumType.error.BannerErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LikeCustomRepositoryImpl implements LikeCustomRepository {

    private final JPAQueryFactory queryFactory;

    private final QBannerLike bannerLike = QBannerLike.bannerLike;

    @Override
    public void deleteLike(Long bannerId, Long memberId) {
        long count = queryFactory.delete(bannerLike)
            .where(bannerLike.banner.id.eq(bannerId), bannerLike.member.id.eq(memberId))
            .execute();
        if(count < 1){
            throw new GeneralException(BannerErrorCode.NOT_EXISTED_LIKE);
        }
    }
}
