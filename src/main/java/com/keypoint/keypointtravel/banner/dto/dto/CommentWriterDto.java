package com.keypoint.keypointtravel.banner.dto.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentWriterDto {
    private Long writerId;
    private String writerName;
    private String profileImageUrl;
}
