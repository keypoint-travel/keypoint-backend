package com.keypoint.keypointtravel.banner.repository.banner;

import com.keypoint.keypointtravel.banner.dto.dto.AdvertisementBannerDto;
import com.keypoint.keypointtravel.banner.dto.dto.AdvertisementDetailDto;
import com.keypoint.keypointtravel.banner.dto.useCase.AdvertisementThumbnailDto;
import com.keypoint.keypointtravel.banner.entity.*;
import com.keypoint.keypointtravel.global.entity.QUploadFile;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.member.entity.QMemberDetail;
import com.querydsl.core.types.Projections;
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
                select(memberDetail.name).from(memberDetail).where(memberDetail.member.id.stringValue().eq(advertisementBannerContent.modifyId)),
                advertisementBannerContent.languageCode
            ))
            .from(advertisementBanner)
            .innerJoin(advertisementBanner.bannerContents, advertisementBannerContent)
            .where(advertisementBanner.isDeleted.isFalse())
            .fetch();
    }

    @Override
    public Long updateIsExposedById(Long bannerId) {
        return queryFactory.update(advertisementBanner)
            .set(advertisementBanner.isDeleted, true)
            .where(advertisementBanner.id.eq(bannerId))
            .execute();
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
    public List<AdvertisementThumbnailDto> findAdvertisementThumbnailList() {
        return queryFactory.select(Projections.constructor(AdvertisementThumbnailDto.class,
                advertisementBanner.id,
                uploadFile.path,
                advertisementBannerContent.mainTitle,
                advertisementBannerContent.subTitle
            ))
            .from(advertisementBanner)
            .innerJoin(advertisementBanner.bannerContents, advertisementBannerContent)
            .leftJoin(uploadFile).on(advertisementBanner.thumbnailImageId.eq(uploadFile.id))
            .where(advertisementBanner.isDeleted.isFalse())
            .fetch();
    }

    @Override
    public boolean isExistBannerContentByLanguageCode(Long bannerId, LanguageCode languageCode) {
        AdvertisementBannerContent bannerContent = queryFactory.selectFrom(advertisementBannerContent)
            .where(advertisementBannerContent.advertisementBanner.id.eq(bannerId)
                .and(advertisementBannerContent.languageCode.eq(languageCode)))
            .fetchOne();
        if (bannerContent != null) {
            return true;
        } else {
            return false;
        }
    }
}
