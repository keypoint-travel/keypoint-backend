package com.keypoint.keypointtravel.banner.repository;

import com.keypoint.keypointtravel.banner.entity.Banner;

import java.util.List;

public interface BannerCustomRepository {

    void deleteBannerById(Long bannerId);

    List<Banner> findBannerList();
}
