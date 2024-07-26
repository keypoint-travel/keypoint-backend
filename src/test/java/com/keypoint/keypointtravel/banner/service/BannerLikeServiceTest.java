package com.keypoint.keypointtravel.banner.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.keypoint.keypointtravel.banner.dto.useCase.ChangeLikeUseCase;
import com.keypoint.keypointtravel.banner.entity.Banner;
import com.keypoint.keypointtravel.banner.repository.banner.BannerRepository;
import com.keypoint.keypointtravel.banner.repository.bannerLike.BannerLikeRepository;
import com.keypoint.keypointtravel.global.enumType.banner.AreaCode;
import com.keypoint.keypointtravel.global.enumType.banner.ContentType;
import com.keypoint.keypointtravel.global.enumType.banner.LargeCategory;
import com.keypoint.keypointtravel.global.enumType.banner.MiddleCategory;
import com.keypoint.keypointtravel.global.enumType.banner.SmallCategory;
import com.keypoint.keypointtravel.global.enumType.member.OauthProviderType;
import com.keypoint.keypointtravel.member.entity.Member;
import com.keypoint.keypointtravel.member.repository.member.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BannerLikeServiceTest {

    @Mock
    private BannerRepository bannerRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BannerLikeRepository bannerLikeRepository;
    @Spy
    @InjectMocks
    private BannerLikeService bannerLikeService;

    @Test
    public void changeLike() {
        //given
        Long bannerId = 1L;
        ChangeLikeUseCase useCaseTrue = new ChangeLikeUseCase(bannerId, true, 1L);
        ChangeLikeUseCase useCaseFalse = new ChangeLikeUseCase(bannerId, false, 1L);
        given(bannerRepository.getReferenceById(any())).willReturn(buildBanner(bannerId, true));
        given(memberRepository.getReferenceById(any())).willReturn(buildMember("khds@naver.com"));

        //when & then  hasILiked = false
        bannerLikeService.changeLike(useCaseFalse);
        verify(bannerRepository, times(1)).getReferenceById(any());
        verify(memberRepository, times(1)).getReferenceById(any());
        verify(bannerLikeRepository, times(0)).deleteLike(any(), any());

        //when & then  hasILiked = true
        bannerLikeService.changeLike(useCaseTrue);
        verify(bannerRepository, times(1)).getReferenceById(any());
        verify(memberRepository, times(1)).getReferenceById(any());
        verify(bannerLikeRepository, times(1)).deleteLike(any(), any());
    }

    private Banner buildBanner(Long bannerId, boolean isExposed) {
        return Banner.builder()
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
    }

    private Member buildMember(String email) {
        return new Member(email, OauthProviderType.GOOGLE);
    }
}
