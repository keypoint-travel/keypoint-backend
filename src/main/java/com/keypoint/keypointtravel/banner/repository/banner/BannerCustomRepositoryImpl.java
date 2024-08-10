package com.keypoint.keypointtravel.banner.repository.banner;


import com.keypoint.keypointtravel.banner.dto.dto.CommentDto;
import com.keypoint.keypointtravel.banner.dto.dto.CommonTourismDto;
import com.keypoint.keypointtravel.banner.dto.useCase.CommonBannerThumbnailDto;
import com.keypoint.keypointtravel.banner.entity.*;
import com.keypoint.keypointtravel.global.entity.QUploadFile;
import com.keypoint.keypointtravel.global.enumType.error.BannerErrorCode;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.member.entity.QMemberDetail;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.querydsl.jpa.JPAExpressions.select;
import static com.querydsl.jpa.JPAExpressions.selectOne;

@RequiredArgsConstructor
public class BannerCustomRepositoryImpl implements BannerCustomRepository {

    private final JPAQueryFactory queryFactory;

    private final QBanner banner = QBanner.banner;

    private final QBannerComment bannerComment = QBannerComment.bannerComment;

    private final QUploadFile uploadFile = QUploadFile.uploadFile;

    private final QBannerContent bannerContent = QBannerContent.bannerContent;

    private final QMemberDetail memberDetail = QMemberDetail.memberDetail;

    @Override
    public void updateIsDeletedById(Long bannerId) {
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
    public void updateContentIsDeletedById(Long bannerId, LanguageCode languageCode) {
        BooleanExpression languageCondition = languageCode == null ? Expressions.TRUE : bannerContent.languageCode.eq(languageCode);
        queryFactory.update(bannerContent)
            .set(bannerContent.isDeleted, true)
            .where(bannerContent.banner.id.eq(bannerId)
                .and(languageCondition))
            .execute();
    }

    @Override
    public boolean existsBannerContentByBannerId(Long bannerId) {
        List<BannerContent> contents = queryFactory.selectFrom(bannerContent)
            .where(bannerContent.banner.id.eq(bannerId).and(bannerContent.isDeleted.isFalse()))
            .fetch();
        System.out.println(contents.size() + " contents.size()");
        if (contents.size() > 0){
            return true;
        }
        return false;
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
    public List<CommonBannerThumbnailDto> findThumbnailList(Long memberId) {
        return queryFactory.select(Projections.constructor(CommonBannerThumbnailDto.class,
                banner.id,
                bannerContent.thumbnailImage,
                bannerContent.mainTitle,
                bannerContent.subTitle))
            .from(banner)
            .innerJoin(banner.bannerContents, bannerContent)
            .where(bannerContent.isDeleted.isFalse().and(bannerContent.languageCode.eq(getMemberLanguage(memberId))))
            .orderBy(bannerContent.modifyAt.desc())
            .fetch();
    }

    private JPQLQuery<LanguageCode> getMemberLanguage(Long memberId) {
        return select(memberDetail.language)
            .from(memberDetail)
            .where(memberDetail.member.id.eq(memberId));
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
            .where(banner.id.eq(bannerId)
                .and(bannerContent.languageCode.eq(languageCode))
                .and(bannerContent.isDeleted.isFalse()))
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

    @Override
    public boolean isExistBannerContentByLanguageCode(Long bannerId, LanguageCode languageCode) {
        BannerContent bannerContent = queryFactory.selectFrom(QBannerContent.bannerContent)
            .where(QBannerContent.bannerContent.banner.id.eq(bannerId)
                .and(QBannerContent.bannerContent.languageCode.eq(languageCode)))
            .fetchOne();
        if (bannerContent != null) {
            return true;
        } else {
            return false;
        }

    }
}
