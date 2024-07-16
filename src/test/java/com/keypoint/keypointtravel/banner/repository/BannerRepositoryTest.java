package com.keypoint.keypointtravel.banner.repository;

import com.keypoint.keypointtravel.banner.dto.dto.CommentDto;
import com.keypoint.keypointtravel.banner.dto.dto.CommonTourismDto;
import com.keypoint.keypointtravel.banner.entity.Banner;
import com.keypoint.keypointtravel.banner.entity.BannerComment;
import com.keypoint.keypointtravel.banner.entity.BannerLike;
import com.keypoint.keypointtravel.global.enumType.banner.*;
import com.keypoint.keypointtravel.global.enumType.member.OauthProviderType;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.member.entity.Member;
import config.QueryDslConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
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

    @Autowired
    private TestEntityManager em;

    @Test
    public void updateIsExposedByIdTest() {
        // given
        Long bannerId = 1L;
        boolean isExposed = true;
        Banner banner = buildBanner(bannerId, isExposed);

        // When & Then
        assertThat(banner.isExposed()).isTrue();
        bannerRepository.updateIsExposedById(bannerId);
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
        Banner banner2 = buildBanner(2L, false);
        Banner banner3 = buildBanner(3L, true);
        em.flush();
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

        //then
        assertThat(dto.getBannerLikesSize()).isEqualTo(1);
        assertThat(dto.isLiked()).isFalse();

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

    private Banner buildBanner(Long bannerId, boolean isExposed) {
        Banner banner = Banner.builder()
            .id(bannerId)
            .title("test")
            .areaCode(AreaCode.BUSAN)
            .cat1(LargeCategory.ACCOMMODATION)
            .cat2(MiddleCategory.AIR_LEISURE_SPORTS)
            .cat3(SmallCategory.AIR_SPORTS)
            .contentType(ContentType.ACCOMMODATION)
            .thumbnailTitle("title")
            .thumbnailImage("image")
            .address1("address1")
            .address2("address2")
            .latitude(37.0)
            .longitude(127.0)
            .isExposed(isExposed)
            .build();
        return em.persist(banner);
    }

    private Member buildMember(String email) {
        return em.persist(new Member(email, OauthProviderType.GOOGLE));
    }

    private BannerComment buildBannerComment(Banner banner, Member member, String content) {
        return em.persist(new BannerComment(banner, member, content));
    }

    private BannerLike buildBannerLike(Banner banner, Member member) {
        return em.persist(new BannerLike(banner, member));
    }
}