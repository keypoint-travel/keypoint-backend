package com.keypoint.keypointtravel.global.handler;

import com.keypoint.keypointtravel.auth.dto.response.TokenInfoDTO;
import com.keypoint.keypointtravel.global.utils.provider.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider tokenProvider;
    @Value("${spring.security.oauth2.authorizedRedirectUri}")
    private String redirectUri;

    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) throws IOException {
        String targetURL = determineTargetUrl(response, authentication);

        getRedirectStrategy().sendRedirect(request, response, targetURL);
    }

    protected String determineTargetUrl(
        HttpServletResponse response,
        Authentication authentication
    ) {
        TokenInfoDTO tokenInfoDTO = tokenProvider.createToken(authentication);
        String jsonResponse = "{\"accessToken\": \"" + tokenInfoDTO.getAccessToken() + "\"}";

        try {
            PrintWriter writer = response.getWriter();
            writer.write(jsonResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);

        return UriComponentsBuilder.fromUriString(redirectUri)
            .queryParam("success", true)
            .queryParam("accessToken", tokenInfoDTO.getAccessToken())
            .queryParam("refreshToken", tokenInfoDTO.getRefreshToken())
            .build().toUriString();
    }
}
