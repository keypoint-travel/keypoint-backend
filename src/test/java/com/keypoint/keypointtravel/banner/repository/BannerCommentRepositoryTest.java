package com.keypoint.keypointtravel.banner.repository;

import com.keypoint.keypointtravel.banner.dto.dto.UpdateCommentDto;
import com.keypoint.keypointtravel.banner.entity.Banner;
import com.keypoint.keypointtravel.banner.entity.BannerComment;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.member.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class BannerCommentRepositoryTest extends RepositoryTest {

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
}
