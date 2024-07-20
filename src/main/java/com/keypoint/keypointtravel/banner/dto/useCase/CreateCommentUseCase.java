package com.keypoint.keypointtravel.banner.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateCommentUseCase {

    private Long bannerId;
    private Long writerId;
    private String content;
}
