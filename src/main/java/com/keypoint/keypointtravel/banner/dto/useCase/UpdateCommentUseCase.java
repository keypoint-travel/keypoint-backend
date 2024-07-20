package com.keypoint.keypointtravel.banner.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateCommentUseCase {

    private Long bannerId;
    private Long commentId;
    private Long memberId;
    private String newContent;
}
