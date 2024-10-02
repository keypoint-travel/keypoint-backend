package com.keypoint.keypointtravel.global.config;

import feign.Retryer;
import java.util.concurrent.TimeUnit;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = {
    "com.keypoint.keypointtravel.banner.service",
    "com.keypoint.keypointtravel.oauth.service",
    "com.keypoint.keypointtravel.currency.service",
    "com.keypoint.keypointtravel.place.service",
    "com.keypoint.keypointtravel.external.azure.service",
    "com.keypoint.keypointtravel.premium.service",
    "com.keypoint.keypointtravel.external.google.service",
})
public class OpenFeignConfig {

    @Bean
    public Retryer feignRetryer() {
        // 1000ms 후 처음 재시도를 하고, 이후 10초 간격으로 최대 10번까지 재시도하도록 설정
        return new Retryer.Default(1000, TimeUnit.SECONDS.toMillis(10), 10);
    }

}