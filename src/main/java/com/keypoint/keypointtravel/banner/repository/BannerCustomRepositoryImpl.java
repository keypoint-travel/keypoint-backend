package com.keypoint.keypointtravel.banner.repository;


import com.keypoint.keypointtravel.banner.entity.Banner;
import com.keypoint.keypointtravel.banner.entity.QBanner;
import com.keypoint.keypointtravel.global.enumType.error.BannerErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BannerCustomRepositoryImpl implements BannerCustomRepository {

    private final JPAQueryFactory queryFactory;

    private final QBanner banner = QBanner.banner;

    @Override
    public void deleteBannerById(Long bannerId) {

        long count = queryFactory.delete(banner)
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
}
