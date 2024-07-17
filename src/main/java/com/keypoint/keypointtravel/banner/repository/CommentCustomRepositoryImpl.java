package com.keypoint.keypointtravel.banner.repository;

import com.keypoint.keypointtravel.banner.dto.dto.UpdateCommentDto;
import com.keypoint.keypointtravel.banner.entity.QBannerComment;
import com.keypoint.keypointtravel.global.enumType.error.BannerErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommentCustomRepositoryImpl implements CommentCustomRepository {

    private final JPAQueryFactory queryFactory;

    private final QBannerComment bannerComment = QBannerComment.bannerComment;

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
}
