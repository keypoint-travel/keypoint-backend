package com.keypoint.keypointtravel.banner.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChangeLikeUseCase {

    private Long id;
    private boolean hasILiked;
    private Long memberId;
}
