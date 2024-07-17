package com.keypoint.keypointtravel.banner.repository;

import com.keypoint.keypointtravel.banner.dto.dto.UpdateCommentDto;

public interface CommentCustomRepository {

    void updateContent(UpdateCommentDto dto);

    void updateIsDeletedById(Long commentId, Long memberId);
}
