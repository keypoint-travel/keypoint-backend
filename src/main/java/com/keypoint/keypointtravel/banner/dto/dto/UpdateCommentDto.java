package com.keypoint.keypointtravel.banner.dto.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateCommentDto {

    private Long commentId;
    private Long memberId;
    private String newContent;
}
