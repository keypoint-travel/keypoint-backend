package com.keypoint.keypointtravel.global.handler;

import com.keypoint.keypointtravel.auth.dto.response.TokenInfoResponse;
import com.keypoint.keypointtravel.auth.redis.service.OAuthTokenService;
import com.keypoint.keypointtravel.global.config.security.CustomUserDetails;
import com.keypoint.keypointtravel.global.enumType.member.RoleType;
import com.keypoint.keypointtravel.global.utils.AuthenticationUtils;
import com.keypoint.keypointtravel.global.utils.LogUtils;
import com.keypoint.keypointtravel.global.utils.provider.JwtTokenProvider;
import com.keypoint.keypointtravel.oauth.service.Oauth2UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider tokenProvider;
    private final Oauth2UserService oauth2UserService;
    private final OAuthTokenService oauthTokenService;
    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) throws IOException {
        String redirectURL = oauth2UserService.getOauthRedirectURL(request);
        String targetURL = determineTargetUrl(redirectURL, response, authentication);

        getRedirectStrategy().sendRedirect(request, response, targetURL);
    }

    protected String determineTargetUrl(
        String redirectURL,
        HttpServletResponse response,
        Authentication authentication
    ) {
        // 1. 토큰 생성
        TokenInfoResponse tokenInfoDTO = tokenProvider.createToken(authentication);
        RoleType role = AuthenticationUtils.getCurrentRoleType(authentication);

        String jsonResponse = "{\"accessToken\": \"" + tokenInfoDTO.getAccessToken() + "\"}";
        try {
            PrintWriter writer = response.getWriter();
            writer.write(jsonResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 2. OAuth 토큰 생성
        manageOauthToken(authentication);

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);

        return UriComponentsBuilder.fromUriString(redirectURL)
            .queryParam("isFirst", role == RoleType.ROLE_CERTIFIED_USER)
            .queryParam("success", true)
            .queryParam("grantType", "Bearer")
            .queryParam("accessToken", tokenInfoDTO.getAccessToken())
            .queryParam("refreshToken", tokenInfoDTO.getRefreshToken())
            .build().toUriString();
    }

    /**
     * 로그인인 경우, OAuth 토큰 저장하는 함수
     *
     * @param authentication
     */
    private void manageOauthToken(Authentication authentication) {
        try {
            RoleType role = AuthenticationUtils.getCurrentRoleType(authentication);

            if (authentication != null && role == RoleType.ROLE_CERTIFIED_USER) {
                OAuth2AuthorizedClient client = oAuth2AuthorizedClientService.loadAuthorizedClient(
                    ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId(),
                    authentication.getName()
                );

                oauthTokenService.saveOAuthToken(
                    ((CustomUserDetails) authentication.getPrincipal()).getId(), client
                );
            }
        } catch (Exception ex) {
            LogUtils.writeInfoLog("OAuth2AuthenticationSuccessHandler",
                "Failed to save oauth token"
            );
        }
    }
}
