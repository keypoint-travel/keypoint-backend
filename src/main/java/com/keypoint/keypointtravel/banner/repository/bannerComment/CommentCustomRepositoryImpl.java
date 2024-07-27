package com.keypoint.keypointtravel.banner.repository.bannerComment;

import com.keypoint.keypointtravel.banner.dto.dto.CommentWriterDto;
import com.keypoint.keypointtravel.banner.dto.dto.UpdateCommentDto;
import com.keypoint.keypointtravel.banner.entity.QBannerComment;
import com.keypoint.keypointtravel.global.entity.QUploadFile;
import com.keypoint.keypointtravel.global.enumType.error.BannerErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.member.entity.QMember;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommentCustomRepositoryImpl implements CommentCustomRepository {

    private final JPAQueryFactory queryFactory;

    private final QBannerComment bannerComment = QBannerComment.bannerComment;

    private final QMember member = QMember.member;

    private final QUploadFile uploadFile = QUploadFile.uploadFile;

    @Override
    public void updateContent(UpdateCommentDto dto) {
        long count = queryFactory.update(bannerComment)
            .set(bannerComment.content, dto.getNewContent())
            .where(bannerComment.id.eq(dto.getCommentId()), bannerComment.member.id.eq(dto.getMemberId()))
            .execute();
        if (count < 1) {
            throw new GeneralException(BannerErrorCode.NOT_EXISTED_COMMENT);
        }
    }

    @Override
    public void updateIsDeletedById(Long commentId, Long memberId) {
        long count = queryFactory.update(bannerComment)
            .set(bannerComment.isDeleted, true)
            .where(bannerComment.id.eq(commentId), bannerComment.member.id.eq(memberId))
            .execute();
        if (count < 1) {
            throw new GeneralException(BannerErrorCode.NOT_EXISTED_COMMENT);
        }
    }

    @Override
    public CommentWriterDto findWriterById(Long writerId) {
        return queryFactory.select(Projections.constructor(CommentWriterDto.class,
                member.id,
                member.memberDetail.name,
                uploadFile.path))
            .from(member)
            .leftJoin(uploadFile).on(member.memberDetail.profileImageId.eq(uploadFile.id))
            .where(member.id.eq(writerId))
            .fetchOne();
    }
}
