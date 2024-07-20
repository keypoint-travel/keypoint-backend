package com.keypoint.keypointtravel.banner.repository;

import com.keypoint.keypointtravel.banner.entity.Banner;
import com.keypoint.keypointtravel.banner.entity.BannerComment;
import com.keypoint.keypointtravel.banner.entity.BannerLike;
import com.keypoint.keypointtravel.global.enumType.banner.*;
import com.keypoint.keypointtravel.global.enumType.member.OauthProviderType;
import com.keypoint.keypointtravel.member.entity.Member;
import config.QueryDslConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@Import(QueryDslConfig.class)
@ActiveProfiles("test")
public abstract class RepositoryTest {

    @Autowired
    protected TestEntityManager em;

    protected Banner buildBanner(Long bannerId, boolean isExposed) {
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

    protected Member buildMember(String email) {
        return em.persist(new Member(email, OauthProviderType.GOOGLE));
    }

    protected BannerComment buildBannerComment(Banner banner, Member member, String content) {
        return em.persist(new BannerComment(banner, member, content));
    }

    protected BannerLike buildBannerLike(Banner banner, Member member) {
        return em.persist(new BannerLike(banner, member));
    }
}
