package com.keypoint.keypointtravel.global.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients("com.keypoint.keypointtravel.banner.service")
public class OpenFeignConfig {

}

