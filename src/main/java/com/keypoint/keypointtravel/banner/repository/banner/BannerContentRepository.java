package com.keypoint.keypointtravel.banner.repository.banner;

import com.keypoint.keypointtravel.banner.entity.BannerContent;
import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerContentRepository extends JpaRepository<BannerContent, Long> {

    Optional<BannerContent> findByBannerIdAndLanguageCode(Long bannerId, LanguageCode languageCode);
}
