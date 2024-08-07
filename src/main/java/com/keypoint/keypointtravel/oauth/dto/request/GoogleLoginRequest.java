package com.keypoint.keypointtravel.oauth.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class GoogleLoginRequest {

    @NotNull
    private String oauthAccessToken;

    @NotNull
    private String oauthRefreshToken;
}
