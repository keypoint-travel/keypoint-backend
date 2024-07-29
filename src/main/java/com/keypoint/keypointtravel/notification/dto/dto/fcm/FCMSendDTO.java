package com.keypoint.keypointtravel.notification.dto.dto.fcm;

import groovy.transform.ToString;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@ToString
@NoArgsConstructor
public class FCMSendDTO {

    private String token;

    private String title;

    private String body;
    
    public FCMSendDTO(String token, String title, String body) {
        this.token = token;
        this.title = title;
        this.body = body;
    }
}
