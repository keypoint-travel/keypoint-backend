package com.keypoint.keypointtravel.banner.dto.dto;

import com.keypoint.keypointtravel.banner.entity.BannerComment;
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

    public static CommentDto from(BannerComment comment) {
        return CommentDto.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .writerId(comment.getMember().getId())
                .writerEmail(comment.getMember().getEmail())
                .createAt(comment.getCreateAt())
                .build();
    }
}
