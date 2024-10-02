package com.keypoint.keypointtravel.notice.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NoticeIdUseCase {

    private Long noticeContentId;
    private Long memberId;

    public static NoticeIdUseCase of(Long noticeContentId, Long memberId) {
        return new NoticeIdUseCase(noticeContentId, memberId);
    }
}
