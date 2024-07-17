package com.keypoint.keypointtravel.banner.repository;

import com.keypoint.keypointtravel.banner.dto.dto.UpdateCommentDto;
import com.keypoint.keypointtravel.banner.entity.Banner;
import com.keypoint.keypointtravel.banner.entity.BannerComment;
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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Import(QueryDslConfig.class)
@ActiveProfiles("test")
public class BannerCommentRepositoryTest {

    @Autowired
    private BannerCommentRepository bannerCommentRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    public void updateContentTest() {
        // given
        Long bannerId = 1L;
        Banner banner = buildBanner(bannerId, true);
        Member member = buildMember("khds@test.com");
        BannerComment comment = buildBannerComment(banner, member, "content");
        String newContent = "newContent";
        System.out.println(comment.getId());

        // when
        bannerCommentRepository.updateContent(new UpdateCommentDto(comment.getId(), member.getId(), newContent));
        em.flush();
        em.clear();

        // then
        Optional<BannerComment> updatedComment = bannerCommentRepository.findById(comment.getId());
        assertThat(updatedComment.get().getContent()).isEqualTo(newContent);

        // when & then
        assertThatThrownBy(() -> bannerCommentRepository.updateContent(
            new UpdateCommentDto(-1L, member.getId(), newContent))).isInstanceOf(GeneralException.class);
        assertThatThrownBy(() -> bannerCommentRepository.updateContent(
            new UpdateCommentDto(comment.getId(), -1L, newContent))).isInstanceOf(GeneralException.class);
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
}
