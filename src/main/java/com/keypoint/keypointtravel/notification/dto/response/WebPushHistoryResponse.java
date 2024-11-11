package com.keypoint.keypointtravel.notification.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WebPushHistoryResponse {

    private Long historyId;
    private String title;
    private String memberName;
    private Long userId;
    private LocalDateTime createAt;

    public static WebPushHistoryResponse of(
        Long historyId,
        String title,
        String memberName,
        Long userId,
        LocalDateTime createAt
    ) {
        return new WebPushHistoryResponse(
            historyId,
            title,
            memberName,
            userId,
            createAt
        );
    }
}
