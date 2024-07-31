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
    private String writerName;
    private String profileImageUrl;
    private LocalDateTime createAt;

    public static CommentDto of(BannerComment comment, CommentWriterDto dto) {
        return CommentDto.builder()
            .commentId(comment.getId())
            .content(comment.getContent())
            .writerId(dto.getWriterId())
            .writerName(dto.getWriterName())
            .profileImageUrl(dto.getProfileImageUrl())
            .createAt(comment.getCreateAt())
            .build();
    }
}
