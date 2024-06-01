package com.keypoint.keypointtravel.config;

import com.keypoint.keypointtravel.common.interceptor.ServiceKeyInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Autowired
  private ServiceKeyInterceptor serviceKeyInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(serviceKeyInterceptor).addPathPatterns("/api/v1/open-api/**");
  }
}
