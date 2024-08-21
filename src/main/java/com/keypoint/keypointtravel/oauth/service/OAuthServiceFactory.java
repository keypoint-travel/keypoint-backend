package com.keypoint.keypointtravel.oauth.service;

import com.keypoint.keypointtravel.global.enumType.member.OauthProviderType;
import com.keypoint.keypointtravel.oauth.service.apple.AppleOAuthService;
import com.keypoint.keypointtravel.oauth.service.google.GoogleOAuthService;
import org.springframework.stereotype.Component;

@Component
public class OAuthServiceFactory {

    private final GoogleOAuthService googleOAuthService;
    private final AppleOAuthService appleOAuthService;

    public OAuthServiceFactory(GoogleOAuthService googleOAuthService,
        AppleOAuthService appleOAuthService) {
        this.googleOAuthService = googleOAuthService;
        this.appleOAuthService = appleOAuthService;
    }

    public OAuthService getService(OauthProviderType providerType) {
        switch (providerType) {
            case GOOGLE:
                return googleOAuthService;
            case APPLE:
                return appleOAuthService;
            default:
                throw new IllegalArgumentException("Unknown provider type: " + providerType);
        }
    }
}