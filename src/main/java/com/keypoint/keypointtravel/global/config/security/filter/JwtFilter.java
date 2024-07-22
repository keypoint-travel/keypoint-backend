package com.keypoint.keypointtravel.global.config.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keypoint.keypointtravel.auth.redis.service.BlacklistService;
import com.keypoint.keypointtravel.global.constants.HeaderConstants;
import com.keypoint.keypointtravel.global.dto.response.ErrorDTO;
import com.keypoint.keypointtravel.global.enumType.error.ErrorCode;
import com.keypoint.keypointtravel.global.enumType.error.TokenErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.global.utils.provider.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtFilter extends GenericFilterBean {

    private static final String TOKEN_GRANT_TYPE = "Bearer";
    private final JwtTokenProvider tokenProvider;
    private final BlacklistService blacklistService;

    @Override
    public void doFilter(
        ServletRequest servletRequest,
        ServletResponse servletResponse,
        FilterChain filterChain
    ) throws IOException, ServletException, GeneralException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        String jwtAccessToken = parseBearerToken(httpServletRequest);
        String requestURI = httpServletRequest.getRequestURI();

        try {
            if (StringUtils.hasText(jwtAccessToken)) {
                TokenErrorCode tokenError = tokenProvider.validateToken(jwtAccessToken);

                switch (tokenError) {
                    case NONE:
                        if (!checkIsLogout(jwtAccessToken)) {
                            Authentication authentication = tokenProvider.getAuthentication(
                                jwtAccessToken);
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        } else {
                            throw new GeneralException(TokenErrorCode.LOGGED_OUT_TOKEN);
                        }
                        break;
                    case EXPIRED_TOKEN:
                        throw new GeneralException(TokenErrorCode.EXPIRED_TOKEN);
                    default:
                        throw new GeneralException(TokenErrorCode.EXPIRED_TOKEN);
                }
            }
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (GeneralException e) {
            setErrorResponse(httpServletResponse, e.getErrorCode());
        }
    }

    private void setErrorResponse(HttpServletResponse response, ErrorCode errorCode)
        throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        ErrorDTO errorDTO = ErrorDTO.from(errorCode);

        ObjectMapper objectMapper = new ObjectMapper();
        String errorJson = objectMapper.writeValueAsString(errorDTO);
        response.getWriter().write(errorJson);
    }

    /**
     * Request Header 에서 토큰 정보를 꺼내오기 위한 함수
     *
     * @param request
     * @return
     */
    private String parseBearerToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HeaderConstants.AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_GRANT_TYPE)) {
            return bearerToken.substring(7);
        }

        return null;
    }

    /**
     * 로그아웃된 토큰인지 확인하는 함수
     *
     * @param accessToken
     * @return
     */
    private boolean checkIsLogout(String accessToken) {
        return blacklistService.existsBlacklistByAccessToken(accessToken);
    }

}
