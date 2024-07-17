package com.keypoint.keypointtravel.banner.repository;

import com.keypoint.keypointtravel.banner.dto.dto.CommentDto;
import com.keypoint.keypointtravel.banner.dto.dto.CommonTourismDto;
import com.keypoint.keypointtravel.banner.entity.Banner;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.member.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class BannerRepositoryTest extends RepositoryTest {

    @Autowired
    private BannerRepository bannerRepository;

    @Test
    public void updateIsExposedByIdTest() {
        // given
        Long bannerId = 1L;
        boolean isExposed = true;
        Banner banner = buildBanner(bannerId, isExposed);

        // When & Then
        assertThat(banner.isExposed()).isTrue();
        bannerRepository.updateIsExposedById(bannerId);
        em.flush();
        em.clear();
        Banner updatedBanner = bannerRepository.findById(bannerId).get();
        assertThat(updatedBanner.isExposed()).isFalse();

        //when & then
        assertThatThrownBy(() -> bannerRepository.updateIsExposedById(100L)).isInstanceOf(GeneralException.class);
    }

    @Test
    public void findBannerListTest() {
        //given
        Banner banner1 = buildBanner(1L, true);
        em.flush();
        Banner banner2 = buildBanner(2L, false);
        em.flush();
        Banner banner3 = buildBanner(3L, true);
        em.flush();
        em.clear();
        //when
        List<Banner> bannerList = bannerRepository.findBannerList();

        //then
        assertThat(bannerList).hasSize(2);
        assertThat(bannerList.get(0).getId()).isEqualTo(banner3.getId());
    }

    @Test
    public void findBannerByIdTest() {
        //given
        Long bannerId = 1L;
        Banner banner = buildBanner(bannerId, true);
        Member member = buildMember("email@test.com");
        Member member2 = buildMember("email2@test.com");
        buildBannerLike(banner, member2);
        em.flush();

        //when
        CommonTourismDto dto = bannerRepository.findBannerById(bannerId, member.getId());
        CommonTourismDto dto2 = bannerRepository.findBannerById(bannerId, null);
        CommonTourismDto dto3 = bannerRepository.findBannerById(bannerId, member2.getId());

        //then
        assertThat(dto.getBannerLikesSize()).isEqualTo(1);
        assertThat(dto.isLiked()).isFalse();
        assertThat(dto2.isLiked()).isFalse();
        assertThat(dto3.isLiked()).isTrue();

        //when & then
        assertThatThrownBy(() -> bannerRepository.findBannerById(100L, member.getId())).isInstanceOf(GeneralException.class);
    }

    @Test
    public void findCommentListByIdTest() {
        //given
        Long bannerId = 1L;
        Banner banner = buildBanner(bannerId, true);
        Member member = buildMember("email@test.com");
        buildBannerComment(banner, member, "test");
        buildBannerComment(banner, member, "test2");
        em.flush();

        //when
        List<CommentDto> dtoList = bannerRepository.findCommentListById(bannerId);

        //then
        assertThat(dtoList).hasSize(2);
    }
}