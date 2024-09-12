package com.keypoint.keypointtravel.notification.dto.response.fcmBodyResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FCMBadgeDetailResponse {

    @JsonProperty(value = "isBadgeEarned")
    private boolean isBadgeEarned;
    private String badgeUrl;

    public static FCMBadgeDetailResponse from(
        boolean isBadgeEarned
    ) {
        return new FCMBadgeDetailResponse(isBadgeEarned, null);
    }

    public static FCMBadgeDetailResponse of(
        boolean isBadgeEarned, String badgeUrl
    ) {
        return new FCMBadgeDetailResponse(isBadgeEarned, isBadgeEarned ? badgeUrl : null);
    }
}
