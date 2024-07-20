package com.keypoint.keypointtravel.banner.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeleteCommentUseCase {

    private Long bannerId;
    private Long commentId;
    private Long memberId;
}
