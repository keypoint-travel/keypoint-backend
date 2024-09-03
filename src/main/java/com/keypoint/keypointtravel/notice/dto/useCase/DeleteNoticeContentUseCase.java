package com.keypoint.keypointtravel.notice.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeleteNoticeContentUseCase {

    private Long[] noticeContentIds;

    public static DeleteNoticeContentUseCase from(Long[] noticeContentIds) {
        return new DeleteNoticeContentUseCase(noticeContentIds);
    }
}
