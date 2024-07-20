package com.keypoint.keypointtravel.banner.repository;

import com.keypoint.keypointtravel.banner.entity.Banner;
import com.keypoint.keypointtravel.banner.entity.BannerLike;
import com.keypoint.keypointtravel.banner.repository.bannerLike.BannerLikeRepository;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.member.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class BannerLikeRepositoryTest extends RepositoryTest {

    @Autowired
    private BannerLikeRepository bannerLikeRepository;

    @Test
    public void deleteLikeTest() {
        // given
        Banner banner = buildBanner(1L, true);
        Member member = buildMember("test@test.com");
        bannerLikeRepository.save(new BannerLike(banner, member));
        em.flush();
        em.clear();

        // when & then
        assertThat(bannerLikeRepository.findAll()).isNotEmpty();
        bannerLikeRepository.deleteLike(banner.getId(), member.getId());
        assertThat(bannerLikeRepository.findAll()).isEmpty();

        assertThatThrownBy(() -> bannerLikeRepository.deleteLike(
            banner.getId(), member.getId())).isInstanceOf(GeneralException.class);
        assertThatThrownBy(() -> bannerLikeRepository.deleteLike(
            -1L, member.getId())).isInstanceOf(GeneralException.class);
    }
}
