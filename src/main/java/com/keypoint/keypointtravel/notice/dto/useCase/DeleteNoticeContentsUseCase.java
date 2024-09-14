package com.keypoint.keypointtravel.notice.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeleteNoticeContentsUseCase {

    private Long[] noticeContentIds;

    public static DeleteNoticeContentsUseCase from(Long[] noticeContentIds) {
        return new DeleteNoticeContentsUseCase(noticeContentIds);
    }
}
