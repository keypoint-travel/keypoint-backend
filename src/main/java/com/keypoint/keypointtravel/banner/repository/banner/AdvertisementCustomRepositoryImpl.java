package com.keypoint.keypointtravel.banner.repository.banner;

import com.keypoint.keypointtravel.banner.dto.dto.AdvertisementBannerDto;
import com.keypoint.keypointtravel.banner.dto.dto.AdvertisementDetailDto;
import com.keypoint.keypointtravel.banner.dto.useCase.advertisement.AdvertisementThumbnailDto;
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

@RequiredArgsConstructor
public class AdvertisementCustomRepositoryImpl implements AdvertisementCustomRepository {

    private final JPAQueryFactory queryFactory;

    private final QAdvertisementBanner advertisementBanner = QAdvertisementBanner.advertisementBanner;

    private final QMemberDetail memberDetail = QMemberDetail.memberDetail;

    private final QAdvertisementBannerContent advertisementBannerContent = QAdvertisementBannerContent.advertisementBannerContent;

    private final QUploadFile uploadFile = QUploadFile.uploadFile;

    @Override
    public List<AdvertisementBannerDto> findAdvertisementBanners() {
        return queryFactory.select(Projections.constructor(AdvertisementBannerDto.class,
                advertisementBanner.id,
                select(uploadFile.path).from(uploadFile).where(uploadFile.id.eq(advertisementBanner.thumbnailImageId)),
                select(uploadFile.path).from(uploadFile).where(uploadFile.id.eq(advertisementBanner.detailImageId)),
                advertisementBannerContent.mainTitle,
                advertisementBannerContent.subTitle,
                advertisementBannerContent.content,
                advertisementBannerContent.createAt,
                select(memberDetail.name).from(memberDetail).where(memberDetail.member.id.stringValue().eq(advertisementBannerContent.registerId)),
                advertisementBannerContent.modifyAt,
                select(memberDetail.name).from(memberDetail).where(memberDetail.member.id.stringValue().eq(advertisementBannerContent.modifyId))
            ))
            .from(advertisementBanner)
            .innerJoin(advertisementBanner.bannerContents, advertisementBannerContent)
            .where(advertisementBanner.isDeleted.isFalse()
                .and(advertisementBannerContent.isDeleted.isFalse())
                .and(advertisementBannerContent.languageCode.eq(LanguageCode.EN)))
            .orderBy(advertisementBannerContent.modifyAt.desc())
            .fetch();
    }

    @Override
    public void updateIsDeletedById(Long bannerId) {
        long count = queryFactory.update(advertisementBanner)
            .set(advertisementBanner.isDeleted, true)
            .where(advertisementBanner.id.eq(bannerId)
                .and(advertisementBanner.isDeleted.isFalse()))
            .execute();
        // 삭제된 배너가 없을 경우
        if (count < 1) {
            throw new GeneralException(BannerErrorCode.NOT_EXISTED_BANNER);
        }
    }

    @Override
    public void updateContentIsDeletedById(Long bannerId, LanguageCode languageCode) {
        BooleanExpression languageCondition = languageCode == null ?
            Expressions.TRUE : advertisementBannerContent.languageCode.eq(languageCode);
        queryFactory.update(advertisementBannerContent)
            .set(advertisementBannerContent.isDeleted, true)
            .where(advertisementBannerContent.advertisementBanner.id.eq(bannerId)
                .and(languageCondition)
                .and(advertisementBannerContent.isDeleted.isFalse()))
            .execute();
    }

    @Override
    public boolean existsBannerContentByBannerId(Long bannerId) {
        List<AdvertisementBannerContent> contents = queryFactory.selectFrom(advertisementBannerContent)
            .where(advertisementBannerContent.advertisementBanner.id.eq(bannerId)
                .and(advertisementBannerContent.isDeleted.isFalse()))
            .fetch();
        if (contents.size() > 0){
            return true;
        }
        return false;
    }

    @Override
    public AdvertisementDetailDto findAdvertisementBannerById(Long bannerId, LanguageCode languageCode) {
        return queryFactory.select(Projections.constructor(AdvertisementDetailDto.class,
                advertisementBanner.id,
                advertisementBannerContent.mainTitle,
                advertisementBannerContent.subTitle,
                advertisementBannerContent.content,
                uploadFile.path
            ))
            .from(advertisementBanner)
            .innerJoin(advertisementBanner.bannerContents, advertisementBannerContent)
            .leftJoin(uploadFile).on(advertisementBanner.detailImageId.eq(uploadFile.id))
            .where(advertisementBanner.id.eq(bannerId)
                .and(advertisementBannerContent.languageCode.eq(languageCode))
                .and(advertisementBannerContent.isDeleted.isFalse()))
            .fetchOne();
    }

    @Override
    public List<AdvertisementThumbnailDto> findAdvertisementThumbnailList(Long memberId) {
        return queryFactory.select(Projections.constructor(AdvertisementThumbnailDto.class,
                advertisementBanner.id,
                uploadFile.path,
                advertisementBannerContent.mainTitle,
                advertisementBannerContent.subTitle
            ))
            .from(advertisementBanner)
            .innerJoin(advertisementBanner.bannerContents, advertisementBannerContent)
            .leftJoin(uploadFile).on(advertisementBanner.thumbnailImageId.eq(uploadFile.id))
            .where(advertisementBannerContent.isDeleted.isFalse()
                .and(advertisementBannerContent.languageCode.eq(getMemberLanguage(memberId))))
            .orderBy(advertisementBannerContent.modifyAt.desc())
            .fetch();
    }

    private JPQLQuery<LanguageCode> getMemberLanguage(Long memberId){
        return select(memberDetail.language)
            .from(memberDetail)
            .where(memberDetail.member.id.eq(memberId));
    }

    @Override
    public boolean isExistBannerContentByLanguageCode(Long bannerId, LanguageCode languageCode) {
        AdvertisementBannerContent bannerContent = queryFactory.selectFrom(advertisementBannerContent)
            .where(advertisementBannerContent.advertisementBanner.id.eq(bannerId)
                .and(advertisementBannerContent.isDeleted.isFalse())
                .and(advertisementBannerContent.languageCode.eq(languageCode)))
            .fetchOne();
        if (bannerContent != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public AdvertisementBannerContent findAdvertisementBanner(Long bannerId, LanguageCode languageCode) {
        AdvertisementBannerContent bannerContent = queryFactory.selectFrom(advertisementBannerContent)
            .where(advertisementBannerContent.advertisementBanner.id.eq(bannerId)
                .and(advertisementBannerContent.isDeleted.isFalse())
                .and(advertisementBannerContent.languageCode.eq(languageCode)))
            .fetchOne();
        if(bannerContent == null){
            throw new GeneralException(BannerErrorCode.NOT_EXISTED_BANNER);
        }
        return bannerContent;
    }

    @Override
    public AdvertisementBanner findAdvertisementBanner(Long bannerId) {
        AdvertisementBanner banner = queryFactory.selectFrom(advertisementBanner)
            .where(advertisementBanner.id.eq(bannerId)
                .and(advertisementBanner.isDeleted.isFalse()))
            .fetchOne();
        if(banner == null){
            throw new GeneralException(BannerErrorCode.NOT_EXISTED_BANNER);
        }
        return banner;
    }
}
