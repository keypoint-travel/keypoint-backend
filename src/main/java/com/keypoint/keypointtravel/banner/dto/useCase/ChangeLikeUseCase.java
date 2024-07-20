package com.keypoint.keypointtravel.banner.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChangeLikeUseCase {

    private Long bannerId;
    private boolean hasILiked;
    private Long memberId;
}
