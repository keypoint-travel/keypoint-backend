package com.keypoint.keypointtravel.notification.dto.request;

import lombok.Getter;

@Getter
public class FCMTestRequest {

    private String deviceToken;
    private String title;
    private String body;
}
