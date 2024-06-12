package com.keypoint.keypointtravel.global.interceptor;

import com.keypoint.keypointtravel.global.constants.HeaderConstants;
import com.keypoint.keypointtravel.global.enumType.error.AuthorityErrorCode;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ServiceKeyInterceptor implements HandlerInterceptor {

    private static String serviceKey;

    @Value("${key.service.key}")
    private void setServiceKey(String serviceKey) {
        this.serviceKey = serviceKey;
    }

    @Override
    public boolean preHandle(
        HttpServletRequest request,
        HttpServletResponse response,
        Object handler
    ) {
        String headerServiceKey = request.getHeader(HeaderConstants.SERVICE_KEY_HEADER);
        if (serviceKey.equals(headerServiceKey)) {
            return true;
        } else {
            throw new GeneralException(HttpStatus.UNAUTHORIZED, AuthorityErrorCode.ACCESS_DENIED);
        }
    }
}
