package com.keypoint.keypointtravel.banner.repository.banner;

import com.keypoint.keypointtravel.banner.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerRepository extends JpaRepository<Banner, Long>, BannerCustomRepository {

}
