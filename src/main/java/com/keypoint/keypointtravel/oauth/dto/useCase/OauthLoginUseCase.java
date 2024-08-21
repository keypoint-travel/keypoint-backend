package com.keypoint.keypointtravel.oauth.dto.useCase;

import com.keypoint.keypointtravel.oauth.dto.request.AppleLoginRequest;
import com.keypoint.keypointtravel.oauth.dto.request.GoogleLoginRequest;
import lombok.Getter;

@Getter
public class OauthLoginUseCase {

    private String oauthAccessToken;
    private String oauthRefreshToken;
    private String oauthCode;

    public OauthLoginUseCase(String oauthCode) {
        this.oauthCode = oauthCode;
    }

    public OauthLoginUseCase(String oauthAccessToken, String oauthRefreshToken) {
        this.oauthAccessToken = oauthAccessToken;
        this.oauthRefreshToken = oauthRefreshToken;
    }

    public static OauthLoginUseCase from(AppleLoginRequest request) {
        return new OauthLoginUseCase(request.getOauthCode());
    }


    public static OauthLoginUseCase from(GoogleLoginRequest request) {
        return new OauthLoginUseCase(request.getOauthAccessToken(), request.getOauthRefreshToken());
    }
}
