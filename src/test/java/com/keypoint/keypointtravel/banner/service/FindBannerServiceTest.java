package com.keypoint.keypointtravel.banner.service;

import com.keypoint.keypointtravel.banner.dto.dto.CommentDto;
import com.keypoint.keypointtravel.banner.dto.dto.CommonTourismDto;
import com.keypoint.keypointtravel.banner.dto.useCase.BannerUseCase;
import com.keypoint.keypointtravel.banner.dto.useCase.CommonTourismUseCase;
import com.keypoint.keypointtravel.banner.repository.banner.BannerRepository;
import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class FindBannerServiceTest {

    @Mock
    private BannerRepository bannerRepository;

    @InjectMocks
    private FindBannerService findBannerService;

    @Test
    public void findBannerTest() {
        // given
        Long memberId = 1L;
        Long bannerId = 1L;
        CommonTourismDto commonTourismDto = buildCommonTourismDto(bannerId);
        List<CommentDto> commentDtoList = new ArrayList<>();
        commentDtoList.add(buildCommentDto(1L, memberId));
        commentDtoList.add(buildCommentDto(2L, memberId));
        CustomUserDetails userDetails = new CustomUserDetails(memberId, "email@test.com", null);
        given(bannerRepository.findBannerById(any(), any())).willReturn(commonTourismDto);
        given(bannerRepository.findCommentListById(any())).willReturn(commentDtoList);

        // when
        CommonTourismUseCase useCase = findBannerService.findCommonBanner(new BannerUseCase(bannerId, userDetails));

        // then
        assertThat(useCase.getCommonTourismDto().getAddress1()).isEqualTo(commonTourismDto.getAddress1());
        assertThat(useCase.getCommentDtoList()).hasSize(2);
        assertThat(useCase.getCommonTourismDto().isLiked()).isTrue();
    }

    private CommonTourismDto buildCommonTourismDto(Long id) {
        return new CommonTourismDto(id, "thumbnailTitle", "title", "address1", "address2", 37.123456, 127.123456, 0, true);
    }

    private CommentDto buildCommentDto(Long commentId, Long writerId) {
        return new CommentDto(commentId, "content", writerId, "email", LocalDateTime.now());
    }
}
