package com.keypoint.keypointtravel.notice.dto.useCase;

import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.notice.dto.request.UpdateNoticeContentRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateNoticeContentUseCase {

    private Long noticeId;
    private Long noticeContentId;
    private LanguageCode languageCode;
    private String title;
    private String content;

    public static UpdateNoticeContentUseCase of(
        Long noticeId,
        Long noticeContentId,
        UpdateNoticeContentRequest useCase
    ) {
        return new UpdateNoticeContentUseCase(
            noticeId,
            noticeContentId,
            useCase.getLanguageCode(),
            useCase.getTitle(),
            useCase.getContent()
        );
    }
}

