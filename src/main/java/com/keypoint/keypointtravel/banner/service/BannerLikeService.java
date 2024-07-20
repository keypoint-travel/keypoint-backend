package com.keypoint.keypointtravel.banner.service;

import com.keypoint.keypointtravel.banner.dto.useCase.ChangeLikeUseCase;
import com.keypoint.keypointtravel.banner.entity.Banner;
import com.keypoint.keypointtravel.banner.entity.BannerLike;
import com.keypoint.keypointtravel.banner.repository.banner.BannerRepository;
import com.keypoint.keypointtravel.banner.repository.bannerLike.BannerLikeRepository;
import com.keypoint.keypointtravel.global.enumType.error.BannerErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.member.entity.Member;
import com.keypoint.keypointtravel.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BannerLikeService {

    private final BannerRepository bannerRepository;

    private final MemberRepository memberRepository;

    private final BannerLikeRepository bannerLikeRepository;

    /**
     * Banner 좋아요 추가 /삭제하는 함수
     *
     * @Param 좋아요 정보 useCase
     */
    @Transactional
    public void changeLike(ChangeLikeUseCase useCase) {
        if (useCase.isHasILiked()) {
            bannerLikeRepository.deleteLike(useCase.getBannerId(), useCase.getMemberId());
            return;
        }
        saveLike(useCase.getBannerId(), useCase.getMemberId());
    }

    private void saveLike(Long bannerId, Long memberId) {
        Banner banner = bannerRepository.getReferenceById(bannerId);
        Member member = memberRepository.getReferenceById(memberId);
        BannerLike like = new BannerLike(banner, member);
        try {
            bannerLikeRepository.save(like);
        } catch (Exception e) {
            throw new GeneralException(BannerErrorCode.NOT_EXISTED_BANNER);
        }
    }
}
