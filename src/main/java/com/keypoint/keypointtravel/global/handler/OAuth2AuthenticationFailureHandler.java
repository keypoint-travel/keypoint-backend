package com.keypoint.keypointtravel.global.handler;

import com.keypoint.keypointtravel.global.exception.GeneralOAuth2AuthenticationException;
import com.keypoint.keypointtravel.oauth.service.Oauth2UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final Oauth2UserService oauth2UserService;

    @Override
    public void onAuthenticationFailure(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException exception
    ) throws IOException {
        String redirectURL = oauth2UserService.getOauthRedirectURL(request);
        String targetURL = null;
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        if (exception.getClass() == GeneralOAuth2AuthenticationException.class) {
            targetURL = determineTargetUrl(
                redirectURL,
                response,
                (GeneralOAuth2AuthenticationException) exception
            );
        } else if (exception.getClass() == OAuth2AuthenticationException.class) {
            targetURL = determineTargetUrl(
                redirectURL,
                response,
                (OAuth2AuthenticationException) exception
            );
        }

        getRedirectStrategy().sendRedirect(request, response, targetURL);
    }

    protected String determineTargetUrl(
        String redirectURL,
        HttpServletResponse response,
        OAuth2AuthenticationException exception
    ) {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);

        return UriComponentsBuilder.fromUriString(redirectURL)
            .queryParam("success", false)
            .queryParam("code", exception.getError().getErrorCode())
            .queryParam("detail", exception.getError().getDescription())
            .build().toUriString();
    }

    protected String determineTargetUrl(
        String redirectURL,
        HttpServletResponse response,
        GeneralOAuth2AuthenticationException exception
    ) {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);

        return UriComponentsBuilder.fromUriString(redirectURL)
            .queryParam("success", false)
            .queryParam("code", exception.getErrorCode().getCode())
            .queryParam("detail", exception.getError().getErrorCode())
            .build().toUriString();
    }
}
