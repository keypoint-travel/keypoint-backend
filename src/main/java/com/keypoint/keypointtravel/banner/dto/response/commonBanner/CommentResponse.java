package com.keypoint.keypointtravel.banner.dto.response.commonBanner;

import com.keypoint.keypointtravel.banner.dto.dto.CommentDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentResponse {
    private Long commentId;
    private String content;
    private Long writerId;
    private String writerName;
    private String profileImageUrl;
    private LocalDateTime createdAt;

    public static CommentResponse from(CommentDto data) {
        return new CommentResponse(
            data.getCommentId(),
            data.getContent(),
            data.getWriterId(),
            data.getWriterName(),
            data.getProfileImageUrl(),
            data.getCreatedAt());
    }
}
