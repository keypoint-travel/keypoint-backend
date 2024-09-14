package com.keypoint.keypointtravel.notice.dto.useCase;

import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.notice.dto.request.CreateNoticeContentRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlusNoticeUseCase {
    private Long noticeId;
    private LanguageCode languageCode;
    private String title;
    private String content;

    public static PlusNoticeUseCase of(
        Long noticeId,
        CreateNoticeContentRequest request
    ) {
        return new PlusNoticeUseCase(
            noticeId,
            request.getLanguageCode(),
            request.getTitle(),
            request.getContent()
        );
    }
}
