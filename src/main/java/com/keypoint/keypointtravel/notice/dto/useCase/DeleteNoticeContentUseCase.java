package com.keypoint.keypointtravel.notice.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeleteNoticeContentUseCase {

    private Long noticeId;
    private Long noticeContentId;

    public static DeleteNoticeContentUseCase of(Long noticeId, Long noticeContentId) {
        return new DeleteNoticeContentUseCase(noticeId, noticeContentId);
    }
}
