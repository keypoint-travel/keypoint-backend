package com.keypoint.keypointtravel.banner.repository.banner;

import com.keypoint.keypointtravel.banner.dto.dto.AdvertisementBannerDto;
import com.keypoint.keypointtravel.banner.entity.QAdvertisementBanner;
import com.keypoint.keypointtravel.global.entity.QUploadFile;
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

    private final QUploadFile uploadFile = QUploadFile.uploadFile;

    @Override
    public List<AdvertisementBannerDto> findAdvertisementBanners() {
        return queryFactory.select(Projections.constructor(AdvertisementBannerDto.class,
                advertisementBanner.id,
                advertisementBanner.title,
                advertisementBanner.content,
                select(uploadFile.path).from(uploadFile).where(uploadFile.id.eq(advertisementBanner.thumbnailImageId)),
                select(uploadFile.path).from(uploadFile).where(uploadFile.id.eq(advertisementBanner.detailImageId)),
                advertisementBanner.createAt,
                select(memberDetail.name).from(memberDetail).where(memberDetail.member.id.stringValue().eq(advertisementBanner.registerId)),
                advertisementBanner.modifyAt,
                select(memberDetail.name).from(memberDetail).where(memberDetail.member.id.stringValue().eq(advertisementBanner.modifyId))
            ))
            .from(advertisementBanner)
            .where(advertisementBanner.isExposed.isTrue())
            .orderBy(advertisementBanner.createAt.desc())
            .fetch();
    }

    @Override
    public Long updateIsExposedById(Long bannerId) {
        return queryFactory.update(advertisementBanner)
            .set(advertisementBanner.isExposed, false)
            .where(advertisementBanner.id.eq(bannerId))
            .execute();
    }
}
