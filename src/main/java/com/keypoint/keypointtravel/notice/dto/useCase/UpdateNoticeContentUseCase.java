package com.keypoint.keypointtravel.notice.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateNoticeContentUseCase {

    private Long noticeContentId;
    private String title;
    private String content;

    public static UpdateNoticeContentUseCase of(
        Long noticeContentId,
        UpdateNoticeContentUseCase useCase
    ) {
        return new UpdateNoticeContentUseCase(
            noticeContentId,
            useCase.getTitle(),
            useCase.getContent()
        );
    }
}

