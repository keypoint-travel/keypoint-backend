package com.keypoint.keypointtravel.config.security.filter;

import com.keypoint.keypointtravel.common.constants.HeaderConstants;
import com.keypoint.keypointtravel.common.enumType.error.TokenErrorCode;
import com.keypoint.keypointtravel.common.exception.GeneralException;
import com.keypoint.keypointtravel.common.utils.provider.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Override
    public void doFilter(
        ServletRequest servletRequest,
        ServletResponse servletResponse,
        FilterChain filterChain
    ) throws IOException, ServletException, GeneralException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String jwtAccessToken = parseBearerToken(httpServletRequest);
        String requestURI = httpServletRequest.getRequestURI();

        if (StringUtils.hasText(jwtAccessToken)
            && tokenProvider.validateToken(jwtAccessToken) == TokenErrorCode.NONE) {
            Authentication authentication = tokenProvider.getAuthentication(jwtAccessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            log.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
        }

        filterChain.doFilter(servletRequest, servletResponse);
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
}
