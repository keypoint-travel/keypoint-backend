package com.keypoint.keypointtravel.oauth.dto.useCase.appleTokenUseCase;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AppleTokenRequestUseCase {

    @JsonProperty(value = "client_id")
    private String clientId;

    @JsonProperty(value = "client_secret")
    private String clientSecret;

    private String code;

    @JsonProperty(value = "grant_type")
    private String grantType = "authorization_code";

    public static AppleTokenRequestUseCase of(
        String clientId,
        String clientSecret,
        String code
    ) {
        return new AppleTokenRequestUseCase(
            clientId,
            clientSecret,
            code,
            "authorization_code"
        );
    }
}
