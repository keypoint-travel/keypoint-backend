package com.keypoint.keypointtravel.notice.dto.response;

import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import com.keypoint.keypointtravel.global.utils.StringUtils;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NoticeResponse {
    private Long noticeId;
    private Long noticeContentId;
    private String title;
    private String content;
    private String thumbnailImageUrl;
    private LanguageCode languageCode;
    private LocalDateTime createAt;
    private LocalDateTime modifyAt;

    public NoticeResponse(
        Long noticeId,
        Long noticeContentId,
        String title,
        String content,
        String thumbnailImageUrl,
        LanguageCode languageCode,
        LocalDateTime createAt,
        LocalDateTime modifyAt
    ) {
        this.noticeId = noticeId;
        this.noticeContentId = noticeContentId;
        this.title = title;
        this.content = StringUtils.stripMarkdown(content);
        this.thumbnailImageUrl = thumbnailImageUrl;
        this.languageCode = languageCode;
        this.createAt = createAt;
        this.modifyAt = modifyAt;
    }

}
