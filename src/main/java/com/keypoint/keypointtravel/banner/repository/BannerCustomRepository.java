package com.keypoint.keypointtravel.banner.repository;

import com.keypoint.keypointtravel.banner.dto.dto.CommonTourismDto;
import com.keypoint.keypointtravel.banner.entity.Banner;

import java.util.List;

public interface BannerCustomRepository {

    void updateIsExposedById(Long bannerId);

    List<Banner> findBannerList();

    CommonTourismDto findBannerById(Long bannerId, Long memberId);
}
