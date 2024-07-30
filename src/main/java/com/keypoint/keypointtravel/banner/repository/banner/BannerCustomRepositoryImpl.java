package com.keypoint.keypointtravel.banner.repository.banner;


import com.keypoint.keypointtravel.banner.dto.dto.CommentDto;
import com.keypoint.keypointtravel.banner.dto.dto.CommonTourismDto;
import com.keypoint.keypointtravel.banner.entity.Banner;
import com.keypoint.keypointtravel.banner.entity.QBanner;
import com.keypoint.keypointtravel.banner.entity.QBannerComment;
import com.keypoint.keypointtravel.banner.entity.QBannerLike;
import com.keypoint.keypointtravel.global.entity.QUploadFile;
import com.keypoint.keypointtravel.global.enumType.error.BannerErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.querydsl.jpa.JPAExpressions.selectOne;

@RequiredArgsConstructor
public class BannerCustomRepositoryImpl implements BannerCustomRepository {

    private final JPAQueryFactory queryFactory;

    private final QBanner banner = QBanner.banner;

    private final QBannerComment bannerComment = QBannerComment.bannerComment;

    private final QUploadFile uploadFile = QUploadFile.uploadFile;

    @Override
    public void updateIsExposedById(Long bannerId) {
        long count = queryFactory.update(banner)
            .set(banner.isExposed, false)
            .where(banner.id.eq(bannerId))
            .execute();
        // 삭제된 배너가 없을 경우
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
        CommonTourismDto dto = queryFactory.select(Projections.constructor(CommonTourismDto.class,
                banner.id,
                banner.thumbnailTitle,
                banner.title,
                banner.address1,
                banner.address2,
                banner.latitude,
                banner.longitude,
                banner.bannerLikes.size(),
                getBannerLikeExpression(bannerId, memberId)))
            .from(banner)
            .where(banner.id.eq(bannerId))
            .fetchOne();
        if(dto == null){
            throw new GeneralException(BannerErrorCode.NOT_EXISTED_BANNER);
        }
         return dto;
    }

    private BooleanExpression getBannerLikeExpression(Long bannerId, Long memberId) {
        if(memberId == null){
            return Expressions.FALSE;
        }
        return selectOne()
            .from(QBannerLike.bannerLike)
            .where(QBannerLike.bannerLike.member.id.eq(memberId)
                .and(QBannerLike.bannerLike.banner.id.eq(bannerId)))
            .exists();
    }

    @Override
    public List<CommentDto> findCommentListById(Long bannerId) {
        return queryFactory.select(Projections.constructor(CommentDto.class,
                bannerComment.id,
                bannerComment.content,
                bannerComment.member.id,
                bannerComment.member.memberDetail.name,
                uploadFile.path,
                bannerComment.createAt))
            .from(bannerComment)
            .leftJoin(uploadFile).on(bannerComment.member.memberDetail.profileImageId.eq(uploadFile.id))
            .where(bannerComment.banner.id.eq(bannerId))
            .orderBy(bannerComment.createAt.desc())
            .fetch();
    }
}
