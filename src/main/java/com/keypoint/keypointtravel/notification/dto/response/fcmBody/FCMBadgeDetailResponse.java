package com.keypoint.keypointtravel.notification.dto.response.fcmBody;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FCMBadgeDetailResponse {

    @JsonProperty(value = "isBadgeEarned")
    private boolean isBadgeEarned;
    private String badgeUrl;
    private String name;

    public static FCMBadgeDetailResponse from(
        boolean isBadgeEarned
    ) {
        return new FCMBadgeDetailResponse(isBadgeEarned, null, null);
    }

    public static FCMBadgeDetailResponse of(
            boolean isBadgeEarned, String badgeUrl,
            String name
    ) {
        return new FCMBadgeDetailResponse(isBadgeEarned, isBadgeEarned ? badgeUrl : null, name);
    }
}
