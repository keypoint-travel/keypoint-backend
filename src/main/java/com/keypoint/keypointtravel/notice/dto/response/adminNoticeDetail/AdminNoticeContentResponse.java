package com.keypoint.keypointtravel.notice.dto.response.adminNoticeDetail;

import com.keypoint.keypointtravel.global.enumType.setting.LanguageCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminNoticeContentResponse {

    private Long noticeContentId;
    private String title;
    private String content;
    private LanguageCode languageCode;
}
