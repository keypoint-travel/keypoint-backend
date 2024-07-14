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

import java.util.List;

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
        // given
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

    @Test
    public  void findBannerListTest() {
        //given
        Banner banner1 = new Banner(1L, AreaCode.BUSAN, LargeCategory.ACCOMMODATION, MiddleCategory.AIR_LEISURE_SPORTS, SmallCategory.AIR_SPORTS, ContentType.ACCOMMODATION, "title", "image", true);
        bannerRepository.save(banner1);
        Banner banner2 = new Banner(2L, AreaCode.SEJONG, LargeCategory.CUISINE, MiddleCategory.SOLO_COURSE, SmallCategory.GATE, ContentType.DINING, "title2", "image2", false);
        bannerRepository.save(banner2);
        Banner banner3 = new Banner(3L, AreaCode.INCHEON, LargeCategory.SHOPPING, MiddleCategory.FAMILY_COURSE, SmallCategory.GOLF, ContentType.TOURIST_COURSE, "title3", "image3", true);
        bannerRepository.save(banner3);

        //when
        List<Banner> bannerList = bannerRepository.findBannerList();

        //then
        assertThat(bannerList).hasSize(2);
        assertThat(bannerList.get(0).getId()).isEqualTo(banner3.getId());
    }
}