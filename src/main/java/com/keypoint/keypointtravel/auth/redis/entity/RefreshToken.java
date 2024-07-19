package com.keypoint.keypointtravel.auth.redis.entity;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@RedisHash(value = "refresh_token")
public class RefreshToken {
    @Id
    private String id;

    @Indexed
    private String email;

    private String refreshToken;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private Long expiration;

    public RefreshToken(String email, String refreshToken, Long expiration) {
        this.email = email;
        this.refreshToken = refreshToken;
        this.expiration = expiration;
    }

    public static RefreshToken of(String email, String refreshToken, Long expiration) {
        return new RefreshToken(email, refreshToken, expiration);
    } 
}
