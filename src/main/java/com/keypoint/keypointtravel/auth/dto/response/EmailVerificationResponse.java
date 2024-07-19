package com.keypoint.keypointtravel.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailVerificationResponse {

    private boolean result;

    public static EmailVerificationResponse from(boolean result) {
        return new EmailVerificationResponse(result);
    }
}
