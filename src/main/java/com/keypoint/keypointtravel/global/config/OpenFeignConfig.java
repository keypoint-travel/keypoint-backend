package com.keypoint.keypointtravel.global.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = {
    "com.keypoint.keypointtravel.banner.service",
    "com.keypoint.keypointtravel.oauth.service"
})
public class OpenFeignConfig {

}