package com.keypoint.keypointtravel.notification.dto.response;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class PushHistoryResponse {

    private Long historyId;
    private String title;
    private String content;
    private LocalDateTime arrivedAt;
}
