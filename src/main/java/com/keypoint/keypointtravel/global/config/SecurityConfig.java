package com.keypoint.keypointtravel.global.config;

import static org.springframework.security.config.Customizer.withDefaults;

import com.keypoint.keypointtravel.auth.redis.service.BlacklistService;
import com.keypoint.keypointtravel.global.config.security.entryPoint.JwtAuthenticationEntryPoint;
import com.keypoint.keypointtravel.global.config.security.filter.JwtFilter;
import com.keypoint.keypointtravel.global.converter.Oauth2RequestEntityConverter;
import com.keypoint.keypointtravel.global.handler.JwtAccessDeniedHandler;
import com.keypoint.keypointtravel.global.handler.OAuth2AuthenticationFailureHandler;
import com.keypoint.keypointtravel.global.handler.OAuth2AuthenticationSuccessHandler;
import com.keypoint.keypointtravel.global.utils.provider.JwtTokenProvider;
import com.keypoint.keypointtravel.oauth.service.Oauth2UserService;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private static final String[] DEFAULT_WHITELIST = {
        "/status", "/images/**", "/error/**, /favicon.ico"
    };
    private static final String[] IGNORE_API = {
        "/api/v1/auth/reissue", "/api/v1/versions"
    };

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final Oauth2UserService customOauth2UserService;
    private final OAuth2AuthenticationSuccessHandler authenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler authenticationFailureHandler;
    private final Oauth2RequestEntityConverter oauth2RequestEntityConverter;
    private final BlacklistService blacklistService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
        DefaultAuthorizationCodeTokenResponseClient accessTokenResponseClient = new DefaultAuthorizationCodeTokenResponseClient();
        accessTokenResponseClient.setRequestEntityConverter(oauth2RequestEntityConverter);
        return accessTokenResponseClient;
    }

    @Bean
    protected SecurityFilterChain config(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .addFilterBefore(
                new JwtFilter(jwtTokenProvider, blacklistService),
                UsernamePasswordAuthenticationFilter.class
            );
        http.exceptionHandling((exceptionConfig) -> exceptionConfig
            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .accessDeniedHandler(jwtAccessDeniedHandler));
        http.authorizeHttpRequests(request -> request
            .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
            .requestMatchers("/**").permitAll()
            .requestMatchers(DEFAULT_WHITELIST).permitAll()
            .requestMatchers(IGNORE_API).permitAll()
            .anyRequest().authenticated()
        );
        http.cors(withDefaults());
        http
            .oauth2Login((oauth2Login) -> oauth2Login
                .tokenEndpoint((tokenEndpoint) -> tokenEndpoint
                    .accessTokenResponseClient(accessTokenResponseClient()))
                .userInfoEndpoint((userInfoEndpoint) -> userInfoEndpoint
                    .userService(customOauth2UserService))
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler));
        return http.build();
    }
}
