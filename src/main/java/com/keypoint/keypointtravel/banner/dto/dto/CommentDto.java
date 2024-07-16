package com.keypoint.keypointtravel.banner.dto.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Long commentId;
    private String content;
    private Long writerId;
    private String writerEmail;
    private LocalDateTime createAt;
}
