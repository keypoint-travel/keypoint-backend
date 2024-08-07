package com.keypoint.keypointtravel.oauth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.keypoint.keypointtravel.auth.dto.response.TokenInfoResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OauthLoginResponse {

    @JsonProperty(value = "isFirst")
    private boolean isFirst;

    private String grandType;
    private String accessToken;
    private String refreshToken;

    public static OauthLoginResponse of(
        boolean isFirst,
        TokenInfoResponse response
    ) {
        return new OauthLoginResponse(
            isFirst,
            response.getGrandType(),
            response.getAccessToken(),
            response.getRefreshToken()
        );
    }
}
