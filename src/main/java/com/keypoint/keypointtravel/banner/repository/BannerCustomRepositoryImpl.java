package com.keypoint.keypointtravel.banner.repository;


import com.keypoint.keypointtravel.banner.dto.dto.CommonTourismDto;
import com.keypoint.keypointtravel.banner.dto.dto.QCommonTourismDto;
import com.keypoint.keypointtravel.banner.entity.Banner;
import com.keypoint.keypointtravel.banner.entity.QBanner;
import com.keypoint.keypointtravel.banner.entity.QBannerComment;
import com.keypoint.keypointtravel.banner.entity.QBannerLike;
import com.keypoint.keypointtravel.global.enumType.error.BannerErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.querydsl.jpa.JPAExpressions.selectOne;

@Repository
@RequiredArgsConstructor
public class BannerCustomRepositoryImpl implements BannerCustomRepository {

    private final JPAQueryFactory queryFactory;

    private final QBanner banner = QBanner.banner;

    @Override
    public void updateIsExposedById(Long bannerId) {

        long count = queryFactory.update(banner)
            .set(banner.isExposed, false)
            .where(banner.id.eq(bannerId))
            .execute();

        if (count < 1) {
            throw new GeneralException(BannerErrorCode.NOT_EXISTED_BANNER);
        }
    }

    @Override
    public List<Banner> findBannerList() {
        return queryFactory.select(banner)
            .from(banner)
            .where(banner.isExposed.isTrue())
            .orderBy(banner.modifyAt.desc())
            .fetch();
    }

    @Override
    public CommonTourismDto findBannerById(Long bannerId, Long memberId) {
        return queryFactory.select(new QCommonTourismDto(
                banner.id,
                banner.thumbnailTitle,
                banner.title,
                banner.address1,
                banner.address2,
                banner.latitude,
                banner.longitude,
                banner.bannerLikes.size(),
                getBannerLikeExpression(bannerId, memberId)
            ))
            .from(banner)
            .leftJoin(banner.bannerLikes, QBannerLike.bannerLike)
            .leftJoin(banner.comments, QBannerComment.bannerComment).fetchJoin()
            .where(banner.id.eq(bannerId))
            .fetchOne();
    }

    private BooleanExpression getBannerLikeExpression(Long bannerId, Long memberId) {
        return selectOne()
            .from(QBannerLike.bannerLike)
            .where(QBannerLike.bannerLike.member.id.eq(memberId)
                .and(QBannerLike.bannerLike.banner.id.eq(bannerId)))
            .exists();
    }
}
