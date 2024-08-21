package com.keypoint.keypointtravel.guide.dto.response;

import com.keypoint.keypointtravel.global.utils.StringUtils;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReadGuideInAdminResponse {

    private Long guideId;
    private String title;
    private String subTitle;
    private String thumbnailImageUrl;
    private String content;
    private int order;
    private LocalDateTime modifyAt;

    public ReadGuideInAdminResponse(
        Long guideId,
        String title,
        String subTitle,
        String thumbnailImageUrl,
        String content,
        int order,
        LocalDateTime modifyAt
    ) {
        this.guideId = guideId;
        this.title = title;
        this.subTitle = subTitle;
        this.thumbnailImageUrl = thumbnailImageUrl;
        this.content = StringUtils.stripMarkdown(content);
        this.order = order;
    }
}
