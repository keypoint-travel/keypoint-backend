package com.keypoint.keypointtravel.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@AllArgsConstructor
public class TokenInfoResponse {

    private String grandType;
    private String accessToken;
    private String refreshToken;

    public static TokenInfoResponse of(
        String grandType,
        String accessToken,
        String refreshToken
    ) {
        return new TokenInfoResponse(grandType, accessToken, refreshToken);
    }

}
