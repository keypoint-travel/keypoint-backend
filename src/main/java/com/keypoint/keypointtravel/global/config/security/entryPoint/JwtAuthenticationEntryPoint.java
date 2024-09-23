package com.keypoint.keypointtravel.global.config.security.entryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keypoint.keypointtravel.global.dto.response.ErrorDTO;
import com.keypoint.keypointtravel.global.enumType.error.ErrorCode;
import com.keypoint.keypointtravel.global.enumType.error.TokenErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException authException
    ) throws IOException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        setErrorResponse(response, TokenErrorCode.EXPIRED_TOKEN);
    }

    private void setErrorResponse(HttpServletResponse response, ErrorCode errorCode)
        throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        ErrorDTO errorDTO = ErrorDTO.from(errorCode);

        ObjectMapper objectMapper = new ObjectMapper();
        String errorJson = objectMapper.writeValueAsString(errorDTO);

        if (response.getWriter() != null) {
            response.getWriter().write(errorJson);
        }
    }
}
