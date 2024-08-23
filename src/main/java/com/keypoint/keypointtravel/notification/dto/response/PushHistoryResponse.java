package com.keypoint.keypointtravel.notification.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PushHistoryResponse {

    private Long historyId;
    private String title;
    private String content;
    private Boolean isRead;
    private LocalDateTime arrivedAt;

    public static PushHistoryResponse of(
        Long historyId,
        String title,
        String content,
        LocalDateTime arrivedAt,
        Boolean isRead
    ) {
        return new PushHistoryResponse(historyId, title, content, isRead, arrivedAt);
    }
}
