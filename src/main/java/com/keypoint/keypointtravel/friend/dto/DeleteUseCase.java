package com.keypoint.keypointtravel.friend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeleteUseCase {

    private Long memberId;
    private Long friendId;
}
