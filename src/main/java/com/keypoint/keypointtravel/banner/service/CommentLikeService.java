package com.keypoint.keypointtravel.banner.service;

import com.keypoint.keypointtravel.banner.dto.useCase.ChangeLikeUseCase;
import com.keypoint.keypointtravel.banner.entity.BannerComment;
import com.keypoint.keypointtravel.banner.entity.BannerCommentLike;
import com.keypoint.keypointtravel.banner.repository.bannerComment.BannerCommentRepository;
import com.keypoint.keypointtravel.banner.repository.bannerComment.CommentLikeRepository;
import com.keypoint.keypointtravel.global.enumType.error.BannerErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.member.entity.Member;
import com.keypoint.keypointtravel.member.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;

    private final BannerCommentRepository bannerCommentRepository;

    private final MemberRepository memberRepository;

    /**
     * BannerComment 좋아요 추가 /삭제하는 함수
     *
     * @Param 좋아요 정보 useCase
     */
    @Transactional
    public void changeLike(ChangeLikeUseCase useCase) {
        // 이미 좋아요를 눌렀을 경우 좋아요 삭제
        if (useCase.isHasILiked()) {
            commentLikeRepository.deleteLike(useCase.getId(), useCase.getMemberId());
            return;
        }
        // 좋아요 추가
        saveLike(useCase.getId(), useCase.getMemberId());
    }

    private void saveLike(Long commentId, Long memberId) {
        // 이미 좋아요가 있는 경우 예외 처리
        if (commentLikeRepository.existsByBannerCommentIdAndMemberId(commentId, memberId)) {
            throw new GeneralException(BannerErrorCode.Exists_LIKE);
        }
        BannerComment comment = bannerCommentRepository.getReferenceById(commentId);
        Member member = memberRepository.getReferenceById(memberId);
        BannerCommentLike like = new BannerCommentLike(comment, member);
        try {
            commentLikeRepository.save(like);
        } catch (Exception e) {
            throw new GeneralException(BannerErrorCode.NOT_EXISTED_COMMENT);
        }
    }
}
