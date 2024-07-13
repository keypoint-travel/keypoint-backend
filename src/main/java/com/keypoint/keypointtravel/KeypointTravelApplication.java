package com.keypoint.keypointtravel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class KeypointTravelApplication {

    public static void main(String[] args) {
        SpringApplication.run(KeypointTravelApplication.class, args);
    }

}
