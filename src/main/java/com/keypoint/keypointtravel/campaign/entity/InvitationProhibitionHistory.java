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
@RedisHash(value = "emailProhibitionHistory")
public class InvitationProhibitionHistory {

    @Id
    private String id;

    @Indexed
    private Long campaignId;

    @TimeToLive(unit = TimeUnit.DAYS)
    private Long expiration;

    public InvitationProhibitionHistory(Long campaignId, Long expiration) {
        this.campaignId = campaignId;
        this.expiration = expiration;
    }
}
