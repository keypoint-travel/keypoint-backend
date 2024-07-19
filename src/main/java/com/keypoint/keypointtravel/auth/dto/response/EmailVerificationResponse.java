package com.keypoint.keypointtravel.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EmailVerificationResponse {
    @JsonProperty(value = "isSame")
    private boolean isSame;
    
    public static EmailVerificationResponse from(boolean isSame) {
        return new EmailVerificationResponse(isSame);
    }
}
