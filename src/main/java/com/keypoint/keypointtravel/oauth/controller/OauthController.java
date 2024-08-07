package com.keypoint.keypointtravel.oauth.controller;

import com.keypoint.keypointtravel.global.dto.response.APIResponseEntity;
import com.keypoint.keypointtravel.global.enumType.member.OauthProviderType;
import com.keypoint.keypointtravel.oauth.dto.request.AppleLoginRequest;
import com.keypoint.keypointtravel.oauth.dto.request.GoogleLoginRequest;
import com.keypoint.keypointtravel.oauth.dto.response.OauthLoginResponse;
import com.keypoint.keypointtravel.oauth.dto.useCase.OauthLoginUseCase;
import com.keypoint.keypointtravel.oauth.service.OAuthService;
import com.keypoint.keypointtravel.oauth.service.OAuthServiceFactory;
import jakarta.validation.Valid;
import java.io.IOException;
import java.security.GeneralSecurityException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/oauth")
public class OauthController {

    private final OAuthServiceFactory oAuthServiceFactory;

    @PostMapping("/login/google")
    public APIResponseEntity<OauthLoginResponse> executeGoogleLogin(
        @Valid @RequestBody GoogleLoginRequest request
    ) throws GeneralSecurityException, IOException {
        OauthLoginUseCase useCase = OauthLoginUseCase.from(request);

        OAuthService oAuthService = oAuthServiceFactory.getService(OauthProviderType.GOOGLE);
        OauthLoginResponse result = oAuthService.login(useCase);

        return APIResponseEntity.<OauthLoginResponse>builder()
            .message("구글 로그인 성공")
            .data(result)
            .build();
    }

    @PostMapping("/login/apple")
    public APIResponseEntity<OauthLoginResponse> executeAppleLogin(
        @Valid @RequestBody AppleLoginRequest request) {
        OauthLoginUseCase useCase = OauthLoginUseCase.from(request);

        OAuthService oAuthService = oAuthServiceFactory.getService(OauthProviderType.APPLE);
        OauthLoginResponse result = oAuthService.login(useCase);

        return APIResponseEntity.<OauthLoginResponse>builder()
            .message("애플 로그인 성공")
            .data(result)
            .build();
    }
}
