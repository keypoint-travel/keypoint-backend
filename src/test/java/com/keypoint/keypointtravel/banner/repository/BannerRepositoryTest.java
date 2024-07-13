package com.keypoint.keypointtravel.banner.repository;

import com.keypoint.keypointtravel.banner.entity.Banner;
import com.keypoint.keypointtravel.global.enumType.banner.*;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import config.QueryDslConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Import(QueryDslConfig.class)
@ActiveProfiles("test")
public class BannerRepositoryTest {

    @Autowired
    private BannerRepository bannerRepository;

    @Test
    public void deleteBannerByIdTest() {
        // Given
        Banner banner = new Banner(1L, AreaCode.BUSAN, LargeCategory.ACCOMMODATION, MiddleCategory.AIR_LEISURE_SPORTS, SmallCategory.AIR_SPORTS, ContentType.ACCOMMODATION, "title", "image", false);
        banner = bannerRepository.save(banner);
        Long bannerId = banner.getId();

        // When & Then
        assertThat(bannerRepository.findAll()).isNotEmpty();
        bannerRepository.deleteBannerById(bannerId);
        assertThat(bannerRepository.findAll()).isEmpty();

        //when & then
        assertThatThrownBy(() -> bannerRepository.deleteBannerById(100L)).isInstanceOf(GeneralException.class);
    }
}
