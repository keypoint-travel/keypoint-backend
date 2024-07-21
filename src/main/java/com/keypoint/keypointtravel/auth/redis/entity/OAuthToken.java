package com.keypoint.keypointtravel.auth.redis.entity;

import jakarta.persistence.Id;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;


@Getter
@NoArgsConstructor
@RedisHash(value = "oauthToken")
public class OAuthToken {

    @Id
    private String id;

    @Indexed
    private Long memberId;

    private String accessToken;

    private LocalDateTime accessTokenExpiredAt;

    private String refreshToken;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private Long refreshTokenExpiration;

    public OAuthToken(Long memberId, OAuth2AuthorizedClient client) {
        OAuth2AccessToken oAuth2AccessToken = client.getAccessToken();
        OAuth2RefreshToken oAuth2RefreshToken = client.getRefreshToken();

        this.memberId = memberId;
        this.accessToken = oAuth2AccessToken.getTokenValue();
        this.accessTokenExpiredAt = oAuth2AccessToken.getExpiresAt() == null
            ? null
            : LocalDateTime.ofInstant(
                oAuth2AccessToken.getExpiresAt(),
                ZoneOffset.UTC
            );

        if (oAuth2RefreshToken != null) {
            this.refreshToken = oAuth2RefreshToken.getTokenValue();
            if (oAuth2RefreshToken.getExpiresAt() == null) {
                this.refreshTokenExpiration = null;
            } else {
                this.refreshTokenExpiration = Instant.now()
                    .until(oAuth2RefreshToken.getExpiresAt(), ChronoUnit.MILLIS);
            }
        }
    }

    public static OAuthToken of(Long memberId, OAuth2AuthorizedClient client) {
        return new OAuthToken(memberId, client);
    }
}
