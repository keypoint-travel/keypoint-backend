package com.keypoint.keypointtravel.global.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = {
    "com.keypoint.keypointtravel.banner.service",
    "com.keypoint.keypointtravel.oauth.service",
    "com.keypoint.keypointtravel.currency.service",
    "com.keypoint.keypointtravel.place.service",
})
public class OpenFeignConfig {

}