package com.keypoint.keypointtravel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@EnableJpaAuditing
@SpringBootApplication
public class KeypointTravelApplication {

    public static void main(String[] args) {
        SpringApplication.run(KeypointTravelApplication.class, args);
    }

}
