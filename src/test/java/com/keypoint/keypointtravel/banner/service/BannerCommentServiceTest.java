package com.keypoint.keypointtravel.banner.service;

import com.keypoint.keypointtravel.banner.dto.useCase.CreateCommentUseCase;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class BannerCommentServiceTest {

    @Autowired
    private BannerCommentService bannerCommentService;

    @Test
    @Transactional
    @DisplayName("댓글 저장 실패 시 예외처리 - 배너 혹은 회원 아이디에 일치하는 정보가 없을 경우 예외 발생")
    public void saveComment() {
        // Given
        CreateCommentUseCase useCase = new CreateCommentUseCase(-1L, -1L, "test");
        // When & then
        assertThatThrownBy(() -> bannerCommentService.saveComment(useCase)).isInstanceOf(GeneralException.class);
    }
}