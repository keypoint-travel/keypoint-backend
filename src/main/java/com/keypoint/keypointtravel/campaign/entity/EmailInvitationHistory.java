package com.keypoint.keypointtravel.campaign.entity;

import jakarta.persistence.Id;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@NoArgsConstructor
@RedisHash(value = "emailRequestHistory")
public class EmailInvitationHistory {

    @Id
    private String id;

    @Indexed
    private Long campaignId;

    private String email;

    @TimeToLive(unit = TimeUnit.DAYS)
    private Long expiration;

    public EmailInvitationHistory(Long campaignId, String email, Long expiration) {
        this.campaignId = campaignId;
        this.email = email;
        this.expiration = expiration;
    }
}
