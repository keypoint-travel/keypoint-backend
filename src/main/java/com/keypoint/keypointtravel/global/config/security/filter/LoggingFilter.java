package com.keypoint.keypointtravel.global.config.security.filter;


import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.keypoint.keypointtravel.global.constants.HeaderConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

@Slf4j
@Component
public class LoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws IOException, ServletException {
        Map<String, Object> requestInfo = new HashMap<>();
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        // objectMapper 설정
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);

        long start = System.currentTimeMillis();
        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
        } finally {
            long end = System.currentTimeMillis();

            // 1. request log 데이터 생성
            requestInfo.put("Method", request.getMethod());
            requestInfo.put("Remote address", request.getRemoteHost());
            requestInfo.put("URI", request.getRequestURI());
            requestInfo.put("Request parameters",
                request.getQueryString() == null ? "" : request.getQueryString());
            requestInfo.put("Response time", end - start);
            requestInfo.put("Accept-Language", request.getHeader(HeaderConstants.LANGUAGE_HEADER));
            requestInfo.put("Response method", request.getHeader(HeaderConstants.LANGUAGE_HEADER));
            requestInfo.put("Request body", getRequestBody(requestWrapper));

            // 2. response 관련 데이터 추가
            int responseStatus = getResponseStatus(responseWrapper);
            requestInfo.put("Response status", responseStatus);
            if (responseStatus >= 300) {
                requestInfo.put("Response body", getResponseBody(responseWrapper));
            }

            // 3. log 쓰기
            logger.info(objectMapper.writeValueAsString(requestInfo));
            responseWrapper.copyBodyToResponse();
        }
    }

    private String getRequestBody(ContentCachingRequestWrapper request) {
        ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request,
            ContentCachingRequestWrapper.class);
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                try {
                    return new String(buf, 0, buf.length, wrapper.getCharacterEncoding());
                } catch (UnsupportedEncodingException e) {
                    return "{}";
                }
            }
        }
        return "{}";
    }

    private int getResponseStatus(final HttpServletResponse response) throws IOException {
        ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response,
            ContentCachingResponseWrapper.class);

        return response.getStatus();
    }

    private String getResponseBody(final HttpServletResponse response) throws IOException {
        String payload = null;
        ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response,
            ContentCachingResponseWrapper.class);
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                payload = new String(buf, 0, buf.length, wrapper.getCharacterEncoding());
            }
        }
        return null == payload ? "{}" : payload;
    }
}
