package com.keypoint.keypointtravel.notice.dto.response.adminNoticeDetail;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminNoticeDetailResponse {

    private Long noticeId;
    private String thumbnailImageUrl;
    private List<AdminNoticeContentResponse> translations;

}
