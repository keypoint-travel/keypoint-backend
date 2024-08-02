package com.keypoint.keypointtravel.global.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CORSConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.setAllowedOrigins(
            Arrays.asList(
                "http://localhost:9000",
                "http://localhost:8000",
                "http://192.168.1.8:8000",
                "http://192.168.1.8:9000",
                "http://192.168.1.8:9500",
                "http://192.168.1.10:9500",  
                "http://10.0.2.2:9500",
                "http://10.0.2.2:9000",
                "http://10.0.2.2:8000",
                "capacitor://",
                "capacitor://localhost",
                "https://m.keypointtravel.com",
                "https://m.keypointtravel.com",
                "https://m-dev.keypointtravel.com",
                "https://appleid.apple.com")
        );
        config.setAllowedHeaders(Collections.singletonList("*"));
        config.setAllowedMethods(
            Arrays.asList("HEAD", "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
