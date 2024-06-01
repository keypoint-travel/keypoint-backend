package com.keypoint.keypointtravel.common.interceptor;

import com.keypoint.keypointtravel.common.constants.HeaderConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
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
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return false;
    }
  }
}
