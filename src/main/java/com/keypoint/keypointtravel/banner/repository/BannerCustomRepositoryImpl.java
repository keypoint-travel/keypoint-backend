package com.keypoint.keypointtravel.banner.repository;


import com.keypoint.keypointtravel.banner.entity.QBanner;
import com.keypoint.keypointtravel.global.enumType.error.BannerErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BannerCustomRepositoryImpl implements BannerCustomRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public void deleteBannerById(Long bannerId) {

        long count = queryFactory.delete(QBanner.banner)
                .where(QBanner.banner.id.eq(bannerId))
                .execute();

        if(count < 1){
            throw new GeneralException(BannerErrorCode.NOT_EXISTED_BANNER);
        }
    }
}
