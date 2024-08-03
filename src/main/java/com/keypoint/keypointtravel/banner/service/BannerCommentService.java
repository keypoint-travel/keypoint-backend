package com.keypoint.keypointtravel.banner.service;

import com.keypoint.keypointtravel.banner.dto.dto.CommentDto;
import com.keypoint.keypointtravel.banner.dto.dto.CommentWriterDto;
import com.keypoint.keypointtravel.banner.dto.dto.UpdateCommentDto;
import com.keypoint.keypointtravel.banner.dto.useCase.CreateCommentUseCase;
import com.keypoint.keypointtravel.banner.dto.useCase.DeleteCommentUseCase;
import com.keypoint.keypointtravel.banner.dto.useCase.UpdateCommentUseCase;
import com.keypoint.keypointtravel.banner.entity.Banner;
import com.keypoint.keypointtravel.banner.entity.BannerComment;
import com.keypoint.keypointtravel.banner.repository.banner.BannerRepository;
import com.keypoint.keypointtravel.banner.repository.bannerComment.BannerCommentRepository;
import com.keypoint.keypointtravel.global.enumType.error.BannerErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.member.entity.Member;
import com.keypoint.keypointtravel.member.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BannerCommentService {

    private final BannerRepository bannerRepository;

    private final MemberRepository memberRepository;

    private final BannerCommentRepository bannerCommentRepository;


    /**
     * BannerComment 생성하는 함수 (배너 댓글 생성)
     *
     * @Param 생성하는 댓글 정보 useCase
     *
     * @Return 생성된 댓글 정보 Dto
     */
    @Transactional
    public CommentDto saveComment(CreateCommentUseCase useCase) {

        Member member = memberRepository.getReferenceById(useCase.getWriterId());
        Banner banner = bannerRepository.getReferenceById(useCase.getBannerId());
        BannerComment comment = new BannerComment(banner, member, useCase.getContent());
        try {
            comment = bannerCommentRepository.save(comment);
        } catch (Exception e) {
            throw new GeneralException(BannerErrorCode.NOT_EXISTED_BANNER);
        }
        // 댓글을 작성한 작성자 정보 조회
        CommentWriterDto dto = bannerCommentRepository.findWriterById(useCase.getWriterId());
        return CommentDto.of(comment, dto);
    }

    /**
     * BannerComment 내용 수정하는 함수 (배너 댓글 수정)
     *
     * @Param 수정하는 댓글 정보 useCase
     */
    @Transactional
    public void updateComment(UpdateCommentUseCase useCase) {
        bannerCommentRepository.updateContent(new UpdateCommentDto(
            useCase.getCommentId(), useCase.getMemberId(), useCase.getNewContent()));
    }

    /**
     * BannerComment 삭제하는 함수(isDeleted를 true로) (배너 댓글 삭제)
     *
     * @Param 삭제하는 댓글 정보 useCase
     */
    @Transactional
    public void deleteComment(DeleteCommentUseCase deleteCommentUseCase) {
        bannerCommentRepository.updateIsDeletedById(
            deleteCommentUseCase.getCommentId(), deleteCommentUseCase.getMemberId());
    }
}