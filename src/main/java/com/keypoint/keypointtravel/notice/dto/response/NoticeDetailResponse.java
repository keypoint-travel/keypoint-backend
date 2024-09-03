package com.keypoint.keypointtravel.notice.dto.response;

import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NoticeDetailResponse {
    private Long noticeId;
    private Long noticeContentId;
    private String title;
    private String content;
    private String thumbnailImageUrl;
    private List<String> detailImagesUrl;
    private LanguageCode languageCode;
    private LocalDateTime createAt;
    private LocalDateTime modifyAt;

    public NoticeDetailResponse(Long noticeId, Long noticeContentId, String title, String content, String thumbnailImageUrl,List<String> detailImagesUrl, LanguageCode languageCode, LocalDateTime createAt, LocalDateTime modifyAt) {
        this.noticeId = noticeId;
        this.noticeContentId = noticeContentId;
        this.title = title;
        this.content = content;
        this.thumbnailImageUrl = thumbnailImageUrl;
        this.detailImagesUrl = detailImagesUrl;
        this.languageCode = languageCode;
        this.createAt = createAt;
        this.modifyAt = modifyAt;
    }

}
