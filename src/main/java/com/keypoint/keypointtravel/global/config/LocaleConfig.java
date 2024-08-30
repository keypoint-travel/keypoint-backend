package com.keypoint.keypointtravel.global.config;

import com.keypoint.keypointtravel.global.constants.HeaderConstants;
import com.keypoint.keypointtravel.member.repository.memberDetail.MemberDetailRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Locale;

@Configurable
public class LocaleConfig implements WebMvcConfigurer {
    private final MemberDetailRepository memberDetailRepository;

    public LocaleConfig(MemberDetailRepository memberDetailRepository) {
        this.memberDetailRepository = memberDetailRepository;
    }

    @Bean
    public LocaleResolver localeResolver() {
        CustomLocaleResolver locale = new CustomLocaleResolver();
        locale.setDefaultLocale(Locale.ENGLISH);
        return locale;
    }


    public static class CustomLocaleResolver extends AcceptHeaderLocaleResolver {

        @Override
        public Locale resolveLocale(HttpServletRequest request) {
            String token = request.getHeader(HeaderConstants.AUTHORIZATION_HEADER);
            String locales = request.getHeader(HeaderConstants.LANGUAGE_HEADER);

            // Authorization 헤더 확인

            // Accept-Language 헤더 확인

            // 모두 존재하지 않는 경우 영어로 설정

            return Locale.getDefault();
        }
    }
}
