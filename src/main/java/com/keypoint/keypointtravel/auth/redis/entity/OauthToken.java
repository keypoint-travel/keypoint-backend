package com.keypoint.keypointtravel.auth.redis.entity;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@RedisHash(value = "oauth_token")
public class OauthToken {
    @Id
    private String id;

    @Indexed
    private String memberId;

    private String accessToken;

    private LocalDateTime accessTokenExpiredAt;

    private String refreshToken;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private Long refreshTokenExpiration;
}
