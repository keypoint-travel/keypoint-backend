package com.keypoint.keypointtravel.global.config;

import com.keypoint.keypointtravel.global.interceptor.ServiceKeyInterceptor;
import com.keypoint.keypointtravel.global.resolver.CustomLocaleResolver;
import com.keypoint.keypointtravel.member.repository.memberDetail.MemberDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final MemberDetailRepository memberDetailRepository;

    public WebConfig(MemberDetailRepository memberDetailRepository) {
        this.memberDetailRepository = memberDetailRepository;
    }

    @Autowired
    private ServiceKeyInterceptor serviceKeyInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(serviceKeyInterceptor).addPathPatterns("/api/v1/open-api/**");
    }

    @Bean
    public LocaleResolver localeResolver() {
        return new CustomLocaleResolver(memberDetailRepository);
    }
}
