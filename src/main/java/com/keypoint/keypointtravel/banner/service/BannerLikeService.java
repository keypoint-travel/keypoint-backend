package com.keypoint.keypointtravel.banner.service;

import com.keypoint.keypointtravel.banner.dto.useCase.ChangeLikeUseCase;
import com.keypoint.keypointtravel.banner.entity.Banner;
import com.keypoint.keypointtravel.banner.entity.BannerLike;
import com.keypoint.keypointtravel.banner.repository.banner.BannerRepository;
import com.keypoint.keypointtravel.banner.repository.bannerLike.BannerLikeRepository;
import com.keypoint.keypointtravel.global.enumType.error.BannerErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.member.entity.Member;
import com.keypoint.keypointtravel.member.repository.member.MemberRepository;
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
        // 이미 좋아요를 눌렀을 경우 좋아요 삭제
        if (useCase.isHasILiked()) {
            bannerLikeRepository.deleteLike(useCase.getId(), useCase.getMemberId());
            return;
        }
        // 좋아요 추가
        saveLike(useCase.getId(), useCase.getMemberId());
    }

    private void saveLike(Long bannerId, Long memberId) {
        // 이미 좋아요가 있는 경우 예외 처리
        if (bannerLikeRepository.existsByBannerIdAndMemberId(bannerId, memberId)) {
            throw new GeneralException(BannerErrorCode.Exists_LIKE);
        }
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
