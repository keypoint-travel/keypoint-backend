package com.keypoint.keypointtravel.banner.repository.banner;


import com.keypoint.keypointtravel.banner.dto.dto.CommentDto;
import com.keypoint.keypointtravel.banner.dto.dto.CommonTourismDto;
import com.keypoint.keypointtravel.banner.entity.*;
import com.keypoint.keypointtravel.global.entity.QUploadFile;
import com.keypoint.keypointtravel.global.enumType.error.BannerErrorCode;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
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

    private final QBannerContent bannerContent = QBannerContent.bannerContent;

    @Override
    public void updateIsExposedById(Long bannerId) {
        long count = queryFactory.update(banner)
            .set(banner.isDeleted, true)
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
            .leftJoin(bannerContent).on(banner.id.eq(bannerContent.banner.id)).fetchJoin()
            .where(banner.isDeleted.isFalse())
            .orderBy(banner.modifyAt.desc())
            .fetch();
    }

    @Override
    public CommonTourismDto findBannerById(LanguageCode languageCode, Long bannerId, Long memberId) {
        CommonTourismDto dto = queryFactory.select(Projections.constructor(CommonTourismDto.class,
                banner.id,
                bannerContent.mainTitle,
                bannerContent.subTitle,
                bannerContent.placeName,
                bannerContent.address1,
                bannerContent.address2,
                banner.latitude,
                banner.longitude,
                banner.bannerLikes.size(),
                getBannerLikeExpression(bannerId, memberId),
                bannerContent.thumbnailImage,
                bannerContent.cat1,
                bannerContent.cat2,
                bannerContent.cat3))
            .from(banner)
            .innerJoin(banner.bannerContents, bannerContent)
            .where(banner.id.eq(bannerId).and(bannerContent.languageCode.eq(languageCode)))
            .fetchOne();
        if (dto == null) {
            throw new GeneralException(BannerErrorCode.NOT_EXISTED_BANNER);
        }
        return dto;
    }

    private BooleanExpression getBannerLikeExpression(Long bannerId, Long memberId) {
        if (memberId == null) {
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
