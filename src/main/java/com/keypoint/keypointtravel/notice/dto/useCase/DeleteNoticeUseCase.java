package com.keypoint.keypointtravel.notice.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeleteNoticeUseCase {

    private Long[] noticeIds;

    public static DeleteNoticeUseCase from(Long[] noticeIds) {
        return new DeleteNoticeUseCase(noticeIds);
    }
}
