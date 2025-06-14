package com.keypoint.keypointtravel.banner.repository.banner;


import static com.querydsl.jpa.JPAExpressions.select;
import static com.querydsl.jpa.JPAExpressions.selectOne;

import com.keypoint.keypointtravel.banner.dto.dto.CommentDto;
import com.keypoint.keypointtravel.banner.dto.dto.CommonTourismDto;
import com.keypoint.keypointtravel.banner.dto.dto.ManageCommonTourismDto;
import com.keypoint.keypointtravel.banner.dto.useCase.CommonBannerThumbnailDto;
import com.keypoint.keypointtravel.banner.dto.useCase.EditBannerUseCase;
import com.keypoint.keypointtravel.banner.entity.BannerContent;
import com.keypoint.keypointtravel.banner.entity.QBanner;
import com.keypoint.keypointtravel.banner.entity.QBannerComment;
import com.keypoint.keypointtravel.banner.entity.QBannerContent;
import com.keypoint.keypointtravel.banner.entity.QBannerLike;
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
import java.util.List;
import lombok.RequiredArgsConstructor;

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
            .where(banner.id.eq(bannerId)
                .and(banner.isDeleted.isFalse()))
            .execute();
        // 삭제된 배너가 없을 경우
        if (count < 1) {
            throw new GeneralException(BannerErrorCode.NOT_EXISTED_BANNER);
        }
    }

    @Override
    public void updateContentIsDeletedById(Long bannerId, LanguageCode languageCode) {
        BooleanExpression languageCondition =
            languageCode == null ? Expressions.TRUE : bannerContent.languageCode.eq(languageCode);
        queryFactory.update(bannerContent)
            .set(bannerContent.isDeleted, true)
            .where(bannerContent.banner.id.eq(bannerId)
                .and(languageCondition)
                .and(bannerContent.isDeleted.isFalse()))
            .execute();
    }

    @Override
    public boolean existsBannerContentByBannerId(Long bannerId) {
        List<BannerContent> contents = queryFactory.selectFrom(bannerContent)
            .where(bannerContent.banner.id.eq(bannerId).and(bannerContent.isDeleted.isFalse()))
            .fetch();
        if (contents.size() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<BannerContent> findBannerList() {
        return queryFactory.select(bannerContent)
            .from(bannerContent)
            .leftJoin(banner).on(banner.id.eq(bannerContent.banner.id)).fetchJoin()
            .where(banner.isDeleted.isFalse().and(bannerContent.isDeleted.isFalse())
                .and(bannerContent.languageCode.eq(LanguageCode.EN)))
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
            .where(bannerContent.isDeleted.isFalse()
                .and(bannerContent.languageCode.eq(getMemberLanguage(memberId))))
            .orderBy(bannerContent.modifyAt.desc())
            .fetch();
    }

    private JPQLQuery<LanguageCode> getMemberLanguage(Long memberId) {
        return select(memberDetail.language)
            .from(memberDetail)
            .where(memberDetail.member.id.eq(memberId));
    }

    @Override
    public CommonTourismDto findBannerById(LanguageCode languageCode, Long bannerId,
        Long memberId) {
        CommonTourismDto dto = queryFactory.select(Projections.constructor(CommonTourismDto.class,
                banner.id,
                bannerContent.contentId,
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

    @Override
    public List<ManageCommonTourismDto> findBannerListById(Long bannerId) {
        List<ManageCommonTourismDto> dtoList = queryFactory.select(
                Projections.constructor(ManageCommonTourismDto.class,
                    banner.id,
                    bannerContent.contentId,
                    bannerContent.languageCode,
                    bannerContent.mainTitle,
                    bannerContent.subTitle,
                    bannerContent.placeName,
                    bannerContent.address1,
                    bannerContent.address2,
                    banner.latitude,
                    banner.longitude,
                    banner.bannerLikes.size(),
                    bannerContent.thumbnailImage,
                    bannerContent.cat1,
                    bannerContent.cat2,
                    bannerContent.cat3))
            .from(banner)
            .innerJoin(banner.bannerContents, bannerContent)
            .where(banner.id.eq(bannerId)
                .and(bannerContent.isDeleted.isFalse()))
            .fetch();
        if (dtoList.isEmpty()) {
            throw new GeneralException(BannerErrorCode.NOT_EXISTED_BANNER);
        }
        return dtoList;
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
                bannerComment.member.name,
                uploadFile.path,
                bannerComment.createAt))
            .from(bannerComment)
            .leftJoin(uploadFile)
            .on(bannerComment.member.memberDetail.profileImageId.eq(uploadFile.id))
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

    @Override
    public void updateBannerContentById(EditBannerUseCase useCase) {
        long count = queryFactory.update(bannerContent)
            .set(bannerContent.mainTitle, useCase.getMainTitle())
            .set(bannerContent.subTitle, useCase.getSubTitle())
            .set(bannerContent.thumbnailImage, useCase.getThumbnailImage())
            .where(bannerContent.banner.id.eq(useCase.getBannerId())
                .and(bannerContent.languageCode.eq(useCase.getLanguage()))
                .and(bannerContent.isDeleted.isFalse()))
            .execute();
        if (count < 1) {
            throw new GeneralException(BannerErrorCode.NOT_EXISTED_BANNER);
        }
    }
}
